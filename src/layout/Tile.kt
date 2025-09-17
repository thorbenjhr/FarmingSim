package layout

import environmental.Environment
import harvesting.Plant
import enums.Action
import enums.Direction
import enums.PlantType
import enums.TileType
import kotlin.math.abs

data class Tile(private val id: Int, private var type: TileType?, internal val coordinates: Coordinate, private val airflow: Boolean, private val direction: Direction?, private var farmId: Int, private val shed: Boolean, private val possiblePlants: List<PlantType>?, private var plant: Plant?, private var dead: Boolean, private var fallowPeriodOver: Int, private val env: Environment?, private var harvestEstimate: Int = 0) {

    fun getId() = id
    fun getType() = type
    fun getCoordinates() = coordinates
    fun getAirflow() = airflow
    fun getDirection() = direction
    fun getFarmId() = farmId
    fun getShed() = shed
    fun getPossiblePlants() = possiblePlants
    fun getPlant() = plant
    fun getDead() = dead
    fun getEnvironment() = env
    fun getHarvestEstimate() = harvestEstimate

    fun setHarvestEstimate(newEstimate: Int) {
        harvestEstimate = newEstimate
    }

    fun setPlant(newPlant: Plant?) {
        plant = newPlant
    }

    fun setTileType(newType: TileType) {
        type = newType
    }

    fun setDead(newDead: Boolean) {
        dead = newDead
    }

    fun setFallowPeriodOver(end: Int) {
        fallowPeriodOver = end
    }

    fun isSquare(): Boolean {
        return abs(coordinates.getX() % 2) == 1
    }

    fun needsAction(action: Action, yearTick: Int): Boolean {
        // for sowing plans to determine whether fallow period over
        if (plant == null) {
            if (action == Action.SOWING) {
                if (type == TileType.FIELD && !dead) {
                    return yearTick >= fallowPeriodOver
                }
            }
        }

        if (dead) return false

        val plantSpec = plant!!.getType().getSpec()
        val lastActions = plant!!.getLastActions()
        val allowedLate = plantSpec.getLatePenaltyMap()[action]?.first ?: 0

        fun lastTickOfAction(action: Action) = lastActions.entries.filter { it.value == action }.maxByOrNull { it.key }?.key
        // tick t before yearTick (wrapping around 1..24 logic)
        fun tickBefore(yearTick: Int, t: Int) = ((yearTick - t - 1 + 24) % 24) + 1
        val lastSowTick = lastTickOfAction(Action.SOWING)

        when (action) { // false when shouldn't be done, true when needs to be done
            Action.SOWING -> {
                if (lastSowTick != null) return false
                if (plantSpec.getSowingSchedule().contains(yearTick)) return true
                if (allowedLate > 0) {
                    for (t in 1..allowedLate) {
                        val check = tickBefore(yearTick, t)
                        if (plantSpec.getSowingSchedule().contains(check)) return true
                    }
                }
                return false
            }
            Action.WEEDING -> {
                val lastSowTick = lastSowTick ?: return false
                val differenceSinceLastSow = ((yearTick - lastSowTick) + 24) % 24

                val lastWeedTick = lastTickOfAction(Action.WEEDING)
                if (lastWeedTick != null && lastTickOfAction(Action.SOWING) != null) {
                    val tickSinceLastWeed = ((lastWeedTick - lastSowTick) + 24) % 24
                    if (tickSinceLastWeed == differenceSinceLastSow) return false
                }

                val weedingSchedule = plantSpec.getWeedSchedule()
                if (weedingSchedule.any{it == differenceSinceLastSow}) return true

                if (allowedLate > 0) {
                    for (offset in weedingSchedule) {
                        if (differenceSinceLastSow in (offset + 1)..(offset + allowedLate)) return true
                    }
                }
                return false
            }
            Action.CUTTING -> {
                val cuttingSchedule = plantSpec.getCuttingSchedule()
                if (cuttingSchedule.contains(yearTick)) return true
                if (allowedLate > 0) {
                    for (t in 1..allowedLate) {
                        val check = tickBefore(yearTick, t)
                        if (plantSpec.getCuttingSchedule().contains(check)) return true
                    }
                }
                return false
            }
            Action.HARVESTING -> {
                val lastHarvestTick = lastTickOfAction(Action.HARVESTING)
                if (lastHarvestTick != null && lastSowTick != null && lastHarvestTick > lastSowTick) return false
                if (lastHarvestTick != null && lastSowTick == null) return false

                val harvestSchedule = plantSpec.getHarvestSchedule()
                if (harvestSchedule.contains(yearTick)) return true

                if (allowedLate > 0) {
                    for (t in 1..allowedLate) {
                        val check = tickBefore(yearTick, t)
                        if (plantSpec.getHarvestSchedule().contains(check)) return true
                    }
                }
                return false
            }
            Action.MOWING -> {
                val mowingSchedule = plantSpec.getMowingSchedule()
                if (mowingSchedule.contains(yearTick)) return true
                if (allowedLate > 0) {
                    for (t in 1..allowedLate) {
                        val check = tickBefore(yearTick, t)
                        if (plantSpec.getMowingSchedule().contains(check)) return true
                    }
                }
                return false
            }
            Action.IRRIGATING -> { return false }
        }
    }

}