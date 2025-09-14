package logging

import Constants
import enums.Action
import enums.PlantType
import environmental.Cloud
import harvesting.Plant
import java.io.PrintWriter

object Logger {
    private var writer = PrintWriter(System.out, true)
    private var logLevel: String = ""

    operator fun invoke(logLevel: String, out: String) {
        this.logLevel = logLevel
        writer = PrintWriter(System.out, true)
    }


    fun logInitializationInfoValid(filename: String) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Initialization Info: $filename successfully parsed and validated.")
        }
    }

    fun logInitializationInfoInvalid(filename: String) {
        writer.println("Initialization Info: $filename is invalid.")
    }

    fun logSimulationStart(startYearTick: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Simulation Info: Simulation started at tick $startYearTick within the year.")
        }
    }

    fun logSimulationEnded(tick: Int) {
        writer.println("Simulation Info: Simulation ended at tick $tick.")
    }

    fun logSimulationTick(tick: Int, yearTick: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Simulation Info: Tick $tick started at tick $yearTick within the year.")
        }
    }

    fun logSoilMoistureBelowThreshold(amountField: Int, amountPlantation: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Soil Moisture: The soil moisture is below threshold in $amountField FIELD and $amountPlantation PLANTATION tiles.")
        }
    }

    fun logCloudRain(cloudID: Int, tileID: Int, amount: Int) {
        writer.println("Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water.")
    }

    fun logCloudMovement(cloudID: Int, amountFluid: Int, startTileID: Int, endTileID: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Cloud Movement: Cloud $cloudID with $amountFluid L water moved from tile $startTileID to tile $endTileID.")
        }
    }

    fun logCloudMovementSunlight(startTileID: Int, amountSunlight: Int) {
        if (logLevel == Constants.DEBUG) {
            writer.println("Cloud Movement: On tile $startTileID, the amount of sunlight is $amountSunlight.")
        }
    }

    fun logCloudUnion(cloudIDFromTile: Int, cloudIDMovingToTile: Int, cloud: Cloud) {
        writer.println("Cloud Union: Clouds $cloudIDFromTile and $cloudIDMovingToTile united to cloud ${cloud.getId()} with ${cloud.getAmount()} L water and duration ${cloud.getDuration()} on tile ${cloud.getLocation()}.")
    }

    fun logCloudStuck(cloudID: Int, tileID: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID.")
        }
    }

    fun logCloudDissipation(cloudID: Int, tileID: Int) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID.")
        }
    }

    fun logCloudPosition(cloudID: Int, tileID: Int, amountSunlight: Int) {
        if (logLevel == Constants.DEBUG) {
            writer.println("Cloud Position: Cloud $cloudID is on tile $tileID, where the amount of sunlight is $amountSunlight.")
        }
    }

    fun logFarmStartingActions(farmID: Int) {
        writer.println(" Farm: Farm $farmID starts its actions.")
    }

    fun logFarmSowingPlan(farmId: Int, sowingPlanIDs: List<Int>) {
        if (logLevel == Constants.DEBUG) {
            writer.println("Farm: Farm $farmId has the following active sowing plans it intends to pursue in this tick: $sowingPlanIDs.")
        }
    }

    fun logFarmAction(machineID: Int, actionType: Action, tileID: Int, duration: Int) {
        writer.println("Farm Action: Machine $machineID performs $actionType on tile $tileID for $duration days.")
    }

    fun logFarmSowing(machineID: Int, plant: Plant, sowingPlanId: Int) {
        writer.println("Farm Sowing: Machine $machineID has sowed $plant according to sowing plan $sowingPlanId.")
    }

    fun logFarmHarvest(machineID: Int, amount: Int, plant: Plant) {
        writer.println("Farm Harvest: Machine $machineID has collected $amount g of $plant harvest.")
    }

    fun logFarmMachineFinishedToShed(machineID: Int, tileID: Int) {
        writer.println("Farm Machine: Machine $machineID is finished and returns to the shed at $tileID.")
    }

    fun logFarmMachineFinishedNoReturn(machineID: Int) {
        writer.println("Farm Machine: Machine $machineID is finished but failed to return.")
    }

    fun logFarmMachineUnloadInShed(machineID: Int, amount: Int, plant: PlantType) {
        writer.println("Farm Machine: Machine $machineID unloads $amount g of $plant harvest in the shed.")
    }

    fun logFarmFinished(farmID: Int) {
        writer.println("Farm: Farm $farmID finished its actions.")
    }

    fun logIncident(incidentID: Int, incidentType: String , tileIDs: List<Int>) {
        writer.println("Incident: Incident $incidentID of type $incidentType happened and affected tiles $tileIDs.")
    }

    fun logHarvestEstimateActionsIncomplete(tileID: Int, actions: List<Action>) {
        if (logLevel == Constants.DEBUG) {
            writer.println("Harvest Estimate: Required actions on tile $tileID were not performed: $actions")
        }
    }

    fun logHarvestEstimateTileUpdate(tileID: Int, amount: Int, plant: Plant) {
        if (logLevel != Constants.IMPORTANT) {
            writer.println("Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of $plant.")
        }
    }

    fun logSimulationStatsCalculated() {
        writer.println("Simulation Info: Simulation statistics are calculated.")
    }

    fun logSimulationStatsFarmHarvest(farmID: Int, amount: Int) {
        writer.println("Simulation Statistics: Farm $farmID collected $amount g of harvest.")
    }

    fun logSimulationStatsPlantHarvest(plant: PlantType, amount: Int) {
        writer.println("Simulation Statistics: Total amount of $plant harvested: $amount g.")
    }

    fun logSimulationStatsTotalHarvest(amount: Int) {
        writer.println("Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g.")
    }
}