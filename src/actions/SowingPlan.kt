package actions

import harvesting.Plant

data class SowingPlan(
    private val id: Int,
    private val tick: Int,
    private val plant: Plant,
    private val fields: List<Int> = emptyList(),
    private val location: Int? = null,
    private val radius: Int? = null
) {
    constructor(id: Int, tick: Int, plant: Plant, fields: List<Int>): this(id, tick, plant, fields, null, null)
    constructor(id: Int, tick: Int, plant: Plant, location: Int, radius: Int): this(id, tick, plant, emptyList(), location, radius)
    fun getId() = id
    fun getTick() = tick
    fun getPlant() = plant
    fun getFields() = fields
    fun getLocation() = location
    fun getRadius() = radius
}