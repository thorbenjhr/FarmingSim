package harvesting

import enums.Action

class PlantSpec(private val initialHarvest: Int, private val requiredMoisture: Int, private val maxSunlightHrs: Int, private val sowSchedule: List<Int>, private val harvestSchedule: List<Int>, private val weedSchedule: List<Int>, private val bloomSchedule: List<Int>, private val cuttingSchedule: List<Int>, private val mowingSchedule: List<Int>, private val requireInsectPollination: Boolean, private val latePenaltyMap: Map<Action, Pair<Int, Double>>) {
    fun getInitialHarvest() = initialHarvest
    fun getRequiredMoisture() = requiredMoisture
    fun getMaxSunlightHours() = maxSunlightHrs
    fun getSowingSchedule() = sowSchedule
    fun getHarvestSchedule() = harvestSchedule
    fun getWeedSchedule() = weedSchedule
    fun getBloomSchedule() = bloomSchedule
    fun getCuttingSchedule() = cuttingSchedule
    fun getMowingSchedule() = mowingSchedule
    fun getInsectPollination() = requireInsectPollination
    fun getLatePenaltyMap() = latePenaltyMap
}