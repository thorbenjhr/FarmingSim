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
        if (currentTick == maxTick) {
            Logger.logSimulationEnded(maxTick)
            return
        }

        while (currentTick < maxTick) {
            tick()
            currentTick++
            yearTick++
            // keep yearTick in 1..24
            yearTick = ((yearTick - 1) % 24 + 24) % 24 + 1
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
        // TODO()
    }

    private fun moveClouds() {
        // TODO()
    }

    private fun assignActionsAndPerform() {
        // TODO()
    }

    private fun startAndEndIncidents() {
        // TODO()
    }

    private fun computeHarvestEstimate() {
        // TODO()
    }
}