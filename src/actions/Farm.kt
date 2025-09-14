package actions

import layout.Tile

data class Farm(private val id: Int, private val name: String, private val farmsteads: Map<Int, Tile>, private var fields: Map<Int, Tile>, private val plantations: Map<Int, Tile>, private var machines: Map<Int, Machine>, private var sowingPlans: Map<Int, SowingPlan>) {
    fun getId() = id
    fun getFarmsteads() = farmsteads
    fun getFields() = fields
    fun getPlantations() = plantations
    fun getMachines() = machines
    fun getSowingPlans() = sowingPlans
}