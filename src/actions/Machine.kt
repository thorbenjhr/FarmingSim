package actions

import enums.Action
import harvesting.Plant

data class Machine(private val id: Int, private val name: String, private val actions: List<Action>, private val plants: List<Plant>, private val durationDays: Int, private var locationTileId: Int, private var brokenIncidentOver: Int, private var assigned: Boolean, private var carryingHarvest: Boolean) {
    fun getBrokenIncidentOver() = brokenIncidentOver
    fun setBrokenIncidentOver(tick: Int) {
        brokenIncidentOver = tick
    }
    fun getId() = id
    fun getName() = name
    fun getActions() = actions
    fun getPlants() = plants
    fun getDurationDays() = durationDays
    fun getLocation() = locationTileId
    fun getAssigned() = assigned
    fun getCarryingHarvest() = carryingHarvest
    fun setCarryingHarvest(newCarryingHarvest: Boolean) {
        carryingHarvest = newCarryingHarvest
    }
    fun setAssigned(newAssigned: Boolean) {
        assigned = newAssigned
    }
    fun setLocation(newLocation: Int) {
        locationTileId = newLocation
    }
}