package actions

import Constants
import enums.Action
import enums.TileType
import layout.MapClass

class FarmHandler(private val bfs: Bfs, map: MapClass) {
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

    fun performActions(farms: Map<Int, Farm>, currentTick: Int, yearTick: Int) {
        farms.toSortedMap().forEach { farm ->
            val tasks = gatherAndSchedule(farm.value, currentTick, yearTick)
            completeTasks(farm.value)
        }
    }

    private fun gatherAndSchedule(farm: Farm, currentTick: Int, yearTick: Int) {
        //getActiveSowingPlans(),collectSowingTargets(),gatherPendingTasks(), prioritizeTasksAssignMachines()
        TODO()
    }

    private fun getActiveSowingPlans(farm: Farm, currentTick: Int): Map<Int, SowingPlan> {
        val activePlans = farm.getSowingPlans() // Map<SowingPlanId, SowingPlan>
            .filter{it.value.getTick() <= currentTick}
        // sorting of ids not implemented
        return activePlans
    }

    private fun collectSowingTargets(farm: Farm, sowingPlans: Map<Int, SowingPlan>, currentTick: Int, map: MapClass): Map<Int, List<Int>> {
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
                        && tile.needsAction(Action.SOWING, currentTick)
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
                        if (tile?.getType() == TileType.FIELD && tile.needsAction(Action.SOWING, currentTick)
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
                    if (field.value.needsAction(action, currentTick)){
                        taskListAllActions.add(Task(1111111111, action, field.value.getPlant(), field.key, TileType.FIELD, currentTick, null, null, null,-1))
                    //////fix id for tasks

                    }
                }
            }
        }
        for (plantation in farm.getPlantations()){
            for (action in Action.entries) {
                if (plantation.value.needsAction(action, currentTick)){
                    taskListAllActions.add(Task(1111111111, action, plantation.value.getPlant(), plantation.key, TileType.PLANTATION, currentTick, null, null, null,-1))
                        //////fix id for tasks
                }
            }
        }
        return taskListAllActions
    }

    private fun prioritizeTasksAssignMachines(farm: Farm, tasks: List<Task>, currentTask: Int): Map<Int, List<Task>> {
        val actionOrder = mapOf(
            Action.SOWING to 0,
            Action.HARVESTING to 1,
            Action.CUTTING to 2,
            Action.IRRIGATING to 3,
            Action.WEEDING to 4,
            Action.MOWING to 5
        )

        val comparator = Comparator<Task> {a,b ->
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

        val priorityQueue = java.util.PriorityQueue<Task>(comparator)
        priorityQueue.addAll(tasks)
        val ordered = mutableListOf<Task>()
        while (priorityQueue.isNotEmpty()) ordered.add(priorityQueue.poll())
        return mapOf(-1 to ordered)
    }

    private fun selectMachineForTask(task: Task) {
        TODO()
    }

    private fun completeTasks(farm: Farm) {
        TODO()
    }

    private fun moveToTileAndApplyChange(taskTile: Int, action: Action) {
        TODO()
    }
}