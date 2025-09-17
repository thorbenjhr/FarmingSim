package actions

import Constants
import enums.Action
import enums.PlantType
import enums.TileType
import layout.MapClass

class FarmHandler(private val bfs: Bfs) {
    fun reduceMoisture(farms: Map<Int, Farm>): Pair<Int, Int> {
        var fields = 0
        var plantations = 0
        farms.toSortedMap().values.forEach { farm ->
            farm.getFields().values.forEach { tile ->
                val plant = tile.getPlant()
                val threshold = plant?.getType()?.getSpec()?.getRequiredMoisture()
                val env = tile.getEnvironment()
                val aboveThreshold = env!!.updateSoil(plant != null, threshold!!)
                if (!aboveThreshold) {
                    fields++
                }
            }

            farm.getPlantations().values.forEach { tile ->
                val plant = tile.getPlant()
                val threshold = plant?.getType()?.getSpec()?.getRequiredMoisture()
                val env = tile.getEnvironment()
                val aboveThreshold = env!!.updateSoil(plant != null, threshold!!)
                if (!aboveThreshold) {
                    plantations++
                }
            }
        }
        return Pair(fields, plantations)
    }

    fun performActions(farms: Map<Int, Farm>, currentTick: Int, yearTick: Int, map: MapClass) {
        farms.toSortedMap().forEach { farm ->
            val tasks = gatherAndSchedule(farm.value, currentTick, yearTick, map)
            completeTasks(farm.value, tasks, currentTick, map)
        }
    }

    private fun gatherAndSchedule(farm: Farm, currentTick: Int, yearTick: Int, map: MapClass): Map<Int, List<Task>> {
        val activeSowingPlans = getActiveSowingPlans(farm, currentTick)

        val allTasks: MutableList<Task> = mutableListOf()
        val sowingTargets = collectSowingTargets(farm, activeSowingPlans, yearTick, map)

        // make sowing targets into tasks
        for ((sowingPlanId, fields) in sowingTargets) {
            val plantToSow = activeSowingPlans[sowingPlanId]?.getPlant()
            for (field in fields) {
                val newTask = Task(Action.SOWING, plantToSow, field, TileType.FIELD , currentTick, null, sowingPlanId, activeSowingPlans[sowingPlanId]?.getTick(), -1)
                allTasks.add(newTask)
            }
        }

        val pendingTasks = gatherPendingTasks(farm, currentTick, yearTick)
        allTasks.addAll(pendingTasks)
        val finalAssignments = prioritizeTasksAssignMachines(farm, allTasks, currentTick)
        return finalAssignments
    }

    private fun getActiveSowingPlans(farm: Farm, currentTick: Int): Map<Int, SowingPlan> {
        val activePlans = farm.getSowingPlans() // Map<SowingPlanId, SowingPlan>
            .filter{it.value.getTick() <= currentTick}
        // sorting of ids not implemented
        return activePlans
    }

    private fun collectSowingTargets(farm: Farm, sowingPlans: Map<Int, SowingPlan>, yearTick: Int, map: MapClass): Map<Int, List<Int>> {
        val readyTargets = mutableMapOf<Int, List<Int>>() //Map<sowingPlanId, List<sowingTargetTileIds>>
        for (sowingPlan in sowingPlans.values) {
            val planId = sowingPlan.getId()
            val planPlant = sowingPlan.getPlant().getType()

            val candidateTiles = mutableListOf<Int>()

            // sowing plans with fields given
            if (sowingPlan.getFields().isNotEmpty()) {
                for (tileId in sowingPlan.getFields()) {
                    val tile = farm.getFields()[tileId]
                    if (tile != null
                        && tile.needsAction(Action.SOWING, yearTick)
                        && tile.getPossiblePlants()?.contains(planPlant) == true
                    ) {
                        candidateTiles.add(tileId)
                    }
                }
            }

            val planLocation = sowingPlan.getLocation()
            val planRadius = sowingPlan.getRadius()
            if (planLocation != null && planRadius != null && planRadius >= 0) {
                val tile = farm.getFields()[planLocation]
                val neighbourCoord = tile?.getCoordinates()?.getNeighbours(radius = planRadius)
                if (neighbourCoord != null) {
                    for (coord in neighbourCoord){
                        val tile = map.getTileByCoordinates(coord)
                        if (tile?.getType() == TileType.FIELD && tile.needsAction(Action.SOWING, yearTick)
                            && tile.getPossiblePlants()?.contains(planPlant) == true
                        ){
                            candidateTiles.add(tile.getId())
                        }
                    }
                }
            }
            readyTargets[planId] = candidateTiles
        }
        return readyTargets
    }

    private fun gatherPendingTasks(farm: Farm, currentTick: Int, yearTick: Int): List<Task> {
        val taskListAllActions = mutableListOf<Task>()
        for (field in farm.getFields()){
            for (action in Action.entries){
                if (action != Action.SOWING){
                    if (field.value.needsAction(action, yearTick)){
                        taskListAllActions.add(Task(action, field.value.getPlant(), field.key, TileType.FIELD, currentTick, null, null, null,-1))
                    }
                }
            }
        }
        for (plantation in farm.getPlantations()){
            for (action in Action.entries) {
                if (plantation.value.needsAction(action, yearTick)){
                    taskListAllActions.add(Task(action, plantation.value.getPlant(), plantation.key, TileType.PLANTATION, currentTick, null, null, null,-1))
                }
            }
        }
        return taskListAllActions
    }

    private fun prioritizeTasksAssignMachines(farm: Farm, tasks: List<Task>, currentTick: Int): Map<Int, List<Task>> {
        val comparator = makeTaskComparator()

        val priorityQueue = java.util.PriorityQueue<Task>(comparator)
        priorityQueue.addAll(tasks)
        val ordered = mutableListOf<Task>()
        while (priorityQueue.isNotEmpty()) ordered.add(priorityQueue.poll())

        val sortedMachines = farm.getMachines().values.sortedWith(compareBy({it.getDurationDays()}, {it.getId()}))
        val tasksWithMachines = assignMachinesToOrderedTasks(ordered, sortedMachines, farm, currentTick)

        return tasksWithMachines
    }

    private fun makeTaskComparator(): Comparator<Task> {
        return Comparator<Task> { a, b ->
            // first complete sowing plans
            val aInSowingPlan: Boolean = a.getSowingPlanId() != null
            val bInSowingPlan: Boolean = b.getSowingPlanId() != null
            if (aInSowingPlan and !bInSowingPlan) return@Comparator -1
            if (!aInSowingPlan and bInSowingPlan) return@Comparator 1

            // sowing plans first by increasing tick then by increasing id
            if (aInSowingPlan and bInSowingPlan) {
                val aTick = a.getSowingPlanTick() ?: Int.MAX_VALUE
                val bTick = b.getSowingPlanTick() ?: Int.MAX_VALUE
                if (aTick != bTick) return@Comparator aTick.compareTo(bTick)

                val aSPId = a.getSowingPlanId() ?: Int.MAX_VALUE
                val bSPId = b.getSowingPlanId() ?: Int.MAX_VALUE
                if (aSPId != bSPId) return@Comparator aSPId.compareTo(bSPId)
            }

            // action ordering as specified
            fun actionOrder(task: Task): Int {
                val tileType = task.getTileType()
                return when (task.getAction()) {
                    Action.SOWING -> if (task.getSowingPlanId() != null) 1 else 9
                    Action.HARVESTING -> if (tileType == TileType.PLANTATION) 2 else 3
                    Action.CUTTING -> 4
                    Action.IRRIGATING -> if (tileType == TileType.FIELD) 5 else 7
                    Action.WEEDING -> 6
                    Action.MOWING -> 8
                }
            }

            val aAction = actionOrder(a)
            val bAction = actionOrder(b)
            if (aAction != bAction) return@Comparator aAction.compareTo(bAction)

            // if all else fails prioritize from lowest to highest id in all tasks
            return@Comparator a.getTileId().compareTo(b.getTileId())
        }
    }

    private fun assignMachinesToOrderedTasks(orderedTasks: MutableList<Task>, sortedMachines: List<Machine>, farm: Farm, currentTick: Int): Map<Int, List<Task>> {
        // assign machines to the ordered tasks
        val assignments = mutableMapOf<Int, MutableList<Task>>()
        val alreadyAssigned = mutableSetOf<Pair<Int, Action>>()

        // determine whether machine can complete action
        fun canMachineDo(machine: Machine, action: Action, plantType: PlantType?): Boolean {
            val machinePlants = machine.getPlants().map { plantType }
            return (machine.getActions().contains(action) and machinePlants.contains(plantType))
            //can plantType be null???????
        }

        fun isReachable(machine: Machine, task: Task): Boolean {
            return try {
                val startPos = machine.getLocation()
                bfs.findPath(startPos, task.getTileId(), farm.getId(), task.getAction() == Action.HARVESTING)
            } catch (_: Exception) {
                false
            }
        }

        fun assignToMachine(machine: Machine, task: Task) {
            assignments.computeIfAbsent(machine.getId()) {mutableListOf()}.add(task)
            alreadyAssigned.add(task.getTileId() to task.getAction())
            task.setShedId(machine.getLocation()) ////DOUBLE CHECK
            machine.setAssigned(true)
        }

        // make new array to hold tasks in their given order and be able to remove as we assign
        val queue = ArrayDeque<Task>(orderedTasks)

        // move down the task list by priority
        while (queue.isNotEmpty()) {
            val task = queue.removeFirst()

            if ((task.getTileId() to task.getAction()) in alreadyAssigned) continue

            val plantType = task.getPlant()?.getType()

            // find first machine in sorted list (by duration, then id) that can perform action on given plant
            // machine needs to not be broken, can perform the action on the plant, and be able to reach tile
            val chosenMachine = sortedMachines.firstOrNull { m ->
                m.getBrokenIncidentOver() <= currentTick &&
                canMachineDo(m, task.getAction(), plantType) &&
                isReachable(m, task)
            }

            // if not machine capable move on
            if (chosenMachine == null) continue

            // determine how many tasks the machine can do within a tick (capacity = number of tasks the machine can complete in a tick)
            val durationOfMachine = chosenMachine.getDurationDays()
            val capacity = if (durationOfMachine <= 0) 0 else (14 / durationOfMachine)
            if (capacity <= 0) {
                // machine can not do a task in a tick (duration > 14)
                continue
            }

            // assign machine to initial task
            assignToMachine(chosenMachine, task)
            var assignedTasksCount = 1

            // while the machine still has duration for another task, get all the nearby tiles
            if (assignedTasksCount < capacity) {
                val containsHarvest = task.getAction() == Action.HARVESTING
                val nearbyMap = try {
                    bfs.findTilesWithinDistance(task.getTileId(), 2, farm.getId(), containsHarvest)
                } catch (_: Exception) {
                    emptyMap()
                }

                if (!nearbyMap.isEmpty()) {
                    val nearbyIds = nearbyMap.keys

                    // look at other tasks in the queue to determine whether machine can complete other tasks
                    val queueSnapshot = queue.toList() // supposedly better than iterating the original set and also removing
                    for (candidate in queueSnapshot) {
                        // if machine full on its max number of tasks per tick then break
                        if (assignedTasksCount >= capacity) break

                        // only tasks with the same action, tile in nearby set, task not already assigned
                        if (candidate.getAction() != task.getAction()) continue
                        if (candidate.getTileId() !in nearbyIds) continue
                        if ((candidate.getTileId() to candidate.getAction()) !in alreadyAssigned) continue

                        // check that machine can do the next task
                        val candidatePlant = candidate.getPlant()?.getType()
                        if (!canMachineDo(chosenMachine, candidate.getAction(), candidatePlant)) continue
                        if (!isReachable(chosenMachine, candidate)) continue

                        // if machine can complete task assign next task to machine
                        assignToMachine(chosenMachine, candidate)
                        assignedTasksCount++

                        // if task can be completed then remove from the original queue
                        queue.remove(candidate)
                    }
                }
            }
        }
        return assignments.mapValues { it.value.toList() }
    }

    private fun moveToTileAndApplyChange(machineId: Int, sowingPlanId: Int?, taskTile: Int, action: Action, tick: Int, map: MapClass) {
        val tile = map.getTileByIndex(taskTile)
        val tilePlant = tile?.getPlant()
        tilePlant?.setLastActions(tick,action)
        if (action == Action.SOWING) {
            tile?.setHarvestEstimate(1111111)
            //logging.Logger.logFarmSowing(machineId, tilePlant, sowingPlanId)
        }
        ////HOW GET HARVEST ESTIMATE AFTER SOWING
        if (action == Action.HARVESTING) {
            val tileHE = tile?.getHarvestEstimate()
            //logging.Logger.logFarmHarvest(machineId, tileHE, tilePlant)
        }
    }

    private fun completeTasks(farm: Farm, tasks: Map<Int, List<Task>>, currentTick: Int, map: MapClass) {
        logging.Logger.logFarmStartingActions(farm.getId())

        for ((machine, taskList) in tasks) {
            for (task in taskList){
                logging.Logger.logFarmAction(machine, task.getAction(), task.getTileId(), farm.getMachines()[machine]!!.getDurationDays())
                //////get correct duration for logging ... from farm
                moveToTileAndApplyChange(machine, task.getSowingPlanId(),task.getTileId(), task.getAction(), currentTick, map)
                //////should be current or year tick
            }
            val stuckMachine = handleShedReturn(farm, machine, taskList)
            if (stuckMachine) {
                taskList.forEach { task ->  }
            }
            val machineObj = farm.getMachines()[machine]
            machineObj?.setAssigned(false)
        }
    }

    private fun handleShedReturn(farm: Farm, machineId: Int, taskList: List<Task>): Boolean {
        val lastTask = taskList.lastOrNull() ?: return false
        if (lastTask.getAction() != Action.HARVESTING) return false
        val machine = farm.getMachines()[machineId] ?: return false
        val fromTile = lastTask.getTileId()
        val originShedTile = machine.getLocation()

        fun pathToShed(from: Int, to: Int, withHarvest: Boolean): Boolean {
            return try {
                bfs.findPath(from, to, farm.getId(), withHarvest)
            } catch (_: Exception) {
                false
            }
        }

        if (pathToShed(fromTile, originShedTile, true)) {
            lastTask.setShedId(originShedTile)
            logging.Logger.logFarmMachineFinishedToShed(machineId, originShedTile)
            return false
        }
        val farmsteadsWithShed = farm.getFarmsteads()
                                    .filter { it.value.getShed() }
                                    .toSortedMap()

        for (farmstead in farmsteadsWithShed.keys) {
            if (farmstead == originShedTile) continue // skip OG farmstead
            if (pathToShed(fromTile, farmstead, true)) {
                lastTask.setShedId(farmstead)
                machine.setLocation(farmstead)
                logging.Logger.logFarmMachineFinishedToShed(machineId, farmstead)
                return false
            }
        }

        // if machine can not get to farmstead remove from machine list
        logging.Logger.logFarmMachineFinishedNoReturn(machineId)
        lastTask.setShedId(-1)
        val newMachines: MutableMap<Int, Machine> = (farm.getMachines() - machineId).toMutableMap()
        farm.setMachines(newMachines)
        return true
    }
}

////SHED LOGIC what happens if machine can't return log, set task id shed to -1, remove machine from farm, how remove harvest?
/// do we log the harvest per harvesting of tile, how remove harvest if machine gets stuck
////FIX LOGGING