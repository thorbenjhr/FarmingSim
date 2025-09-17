import actions.Farm
import actions.FarmHandler
import environmental.CloudManager
import harvesting.HarvestEstimator
import incidents.IncidentManager
import layout.MapClass
import logging.Logger

class Simulation (private val m: MapClass, private val farms: Map<Int, Farm>, private val farmHandler: FarmHandler, private val cm: CloudManager, private val harvestEstimator: HarvestEstimator, private val incidentManager: IncidentManager, private var yearTick: Int, private val maxTick: Int) {
    private var currentTick = Constants.NO_VALUE
    fun start() {
        Logger.logSimulationStart(yearTick)
        while (currentTick < maxTick) {
            tick()
            currentTick++
            yearTick++
            // keep yearTick in 1..24
            yearTick = ((yearTick - 1) % 24 + 24) % 24 + 1
        }

        if (currentTick == maxTick) {
            Logger.logSimulationEnded(maxTick)
            harvestEstimator.logSimulationStatistics()
        }
    }

    private fun tick() {
        Logger.logSimulationTick(currentTick, yearTick)
        reduceSoilMoisture()
        moveClouds()
        assignActionsAndPerform()
        startAndEndIncidents()
        computeHarvestEstimate()
    }

    private fun reduceSoilMoisture() {
        val (fields, plantations) = farmHandler.reduceMoisture(farms)
        Logger.logSoilMoistureBelowThreshold(fields, plantations)
    }

    private fun moveClouds() {
        cm.applyMoves()
    }

    private fun assignActionsAndPerform() {
        farmHandler.performActions(farms, currentTick, yearTick, m)
    }

    private fun startAndEndIncidents() {
        incidentManager.handleIncidents(currentTick, cm, m, farms)
    }

    private fun computeHarvestEstimate() {
        harvestEstimator.computeAllTiles(yearTick, farms)
    }
}