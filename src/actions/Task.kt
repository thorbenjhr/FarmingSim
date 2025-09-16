package actions

import enums.Action
import enums.TileType
import harvesting.Plant

data class Task(private var action: Action, private val plant: Plant?, private val tileId: Int, private val tileType: TileType, private val currentTick: Int, private var assignedMachine: Machine?, private var sowingPlanId: Int?, private var sowingPlanTick: Int?, private var shedId: Int = -1) {
    fun getAction() = action
    fun getPlant() = plant
    fun getTileId() = tileId
    fun getTileType() = tileType
    fun getCurrentTick() = currentTick
    fun getAssignedMachine() = assignedMachine
    fun setAssignedMachine(machine: Machine) {
        assignedMachine = machine
    }
    fun getSowingPlanId() = sowingPlanId
    fun getSowingPlanTick() = sowingPlanTick
    fun getShedId() = shedId
    fun setShedId(newId: Int) {
        shedId = newId
    }
}