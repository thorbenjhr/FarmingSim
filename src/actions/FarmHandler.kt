package actions

import Constants
import enums.Action

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

    fun performActions(farms: Map<Int, Farm>, currentTick: Int, yearTick: Int) {
    }

    private fun gatherAndSchedule(currentTick: Int, yearTick: Int) {
        TODO()
    }

    private fun getActiveSowingPlans(currentTick: Int): Map<Int, SowingPlan> {
        TODO()
    }

    private fun collectSowingTargets(sowingPlans: Map<Int, SowingPlan>): Map<Int, List<Int>> {
        TODO()
    }

    private fun gatherPendingTasks(currentTick: Int, yearTick: Int): List<Task> {
        TODO()
    }

    private fun prioritizeTasksAssignMachines(tasks: List<Task>): Map<Int, List<Task>> {
        TODO()
    }

    private fun selectMachineForTask(task: Task) {
        TODO()
    }

    private fun completeTasks() {
        TODO()
    }

    private fun moveToTileAndApplyChange(taskTile: Int, action: Action) {
        TODO()
    }
}