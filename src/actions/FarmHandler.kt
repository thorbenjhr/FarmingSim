package actions

import Constants
import enums.Action

class FarmHandler(private val bfs: Bfs) {
    fun reduceMoisture(farms: Map<Int, Farm>): Pair<Int, Int> {
        // TODO()
    return Pair(Constants.NO_VALUE, Constants.NO_VALUE)
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