package harvesting

import enums.Action

class PlantSpec(private val initialHarvest: Int, private val requiredMoisture: Int, private val maxSunlightHrs: Int, private val sowSchedule: List<Int>, private val harvestSchedule: List<Int>, private val weedSchedule: List<Int>, private val bloomSchedule: List<Int>, private val cuttingSchedule: List<Int>, private val mowingSchedule: List<Int>, private val requireInsectPollination: Boolean, private val latePenaltyMap: Map<Action, Pair<Int, Double>>) {
    fun getRequiredMoisture() = requiredMoisture
}