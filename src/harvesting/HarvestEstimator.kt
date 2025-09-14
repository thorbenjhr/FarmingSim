package harvesting

import Constants
import actions.Farm
import enums.PlantType
import environmental.Environment
import logging.Logger

class HarvestEstimator {
    private val totalHarvested = mutableMapOf<Int, Map<PlantType, Int>>()
    private var currentEstimate = 0

    fun computeAllTiles(yearTick: Int, farms: Map<Int, Farm>) {
        farms.values.forEach { farm ->
            farm.getFields().values.forEach { tile ->
                val plant = tile.getPlant()?: return
                val env = tile.getEnvironment()?: return
                var estimate = tile.getHarvestEstimate()

                estimate = applySowingLate(estimate, plant)
                estimate = applySunlightPenalty(estimate, env, plant)
                estimate = applyMoisturePenalty(estimate, env, plant)
                estimate = applyActionPenalties(estimate, plant)
                estimate = applyIncidentEffect(estimate, env, plant)

                tile.setHarvestEstimate(estimate)
            }

            farm.getPlantations().values.forEach { tile ->
                val plant = tile.getPlant()?: return
                val env = tile.getEnvironment()?: return
                var estimate = tile.getHarvestEstimate()

                estimate = applySowingLate(estimate, plant)
                estimate = applySunlightPenalty(estimate, env, plant)
                estimate = applyMoisturePenalty(estimate, env, plant)
                estimate = applyActionPenalties(estimate, plant)
                estimate = applyIncidentEffect(estimate, env, plant)

                tile.setHarvestEstimate(estimate)
            }
        }
    }

    private fun applySowingLate(currentEst: Int, plant: Plant): Int {
        TODO()
    }

    private fun applySunlightPenalty(currentEst: Int, env: Environment, plant: Plant): Int {
        TODO()
    }

    private fun applyMoisturePenalty(currentEst: Int, env: Environment, plant: Plant): Int {
        TODO()
    }

    private fun applyActionPenalties(currentEst: Int, plant: Plant): Int {
        TODO()
    }

    private fun applyIncidentEffect(currentEst: Int, env: Environment, plant: Plant): Int {
        TODO()
    }

    fun logSimulationStatistics() {
        // for example only
        totalHarvested[3] = mapOf(
            PlantType.OAT to 5,
            PlantType.GRAPE to 3
        )

        totalHarvested[7] = mapOf(
            PlantType.OAT to 1,
            PlantType.POTATO to 2,
            PlantType.CHERRY to 11,
            PlantType.ALMOND to 3
        )

        currentEstimate = 4

        Logger.logSimulationStatsCalculated()

        totalHarvested.keys.sorted().forEach { id ->
            val farmHarvestMap = totalHarvested[id]?: emptyMap()
            val totalAmount = farmHarvestMap.values.sum()
            Logger.logSimulationStatsFarmHarvest(id, totalAmount)
        }

        val totalPerPlant: Map<PlantType, Int> = totalHarvested.values.flatMap { it.entries }.groupingBy { it.key }.fold(Constants.NO_VALUE) { acc, entry -> acc + entry.value }
        totalPerPlant.forEach { (plant, amount) -> Logger.logSimulationStatsPlantHarvest(plant, amount)}

        Logger.logSimulationStatsTotalHarvest(currentEstimate)
    }
}