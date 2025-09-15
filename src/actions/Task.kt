package actions

import enums.Action
import harvesting.Plant

data class Task(private val id: Int, private var action: Action, private val plant: Plant?, private val tileId: Int, private val currentTick: Int, private var assignedMachine: Machine?, private var shedId: Int = -1) {
    fun getId() = id
    fun getAction() = action
    fun getPlant() = plant
    fun getTileId() = tileId
    fun getCurrentTick() = currentTick
    fun getAssignedMachine() = assignedMachine
    fun setAssignedMachine(machine: Machine) {
        assignedMachine = machine
    }
    fun getShedId() = shedId
    fun setShedId(newId: Int) {
        shedId = newId
    }
}