package incidents

import enums.IncidentType

data class Incident(private val id: Int, private val tick: Int, private val type: IncidentType, private val location: Int, private val radius: Int, private val duration: Int, private val amount: Int, private val machineId: Int, private val effect: Int) {
    fun getId() = id
    fun getTick() = tick
    fun getType() = type
    fun getLocation() = location
    fun getRadius() = radius
    fun getDuration() = duration
    fun getAmount() = amount
    fun getMachineId() = machineId
    fun getEffect() = effect
}