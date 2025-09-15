package layout

import environmental.Environment
import harvesting.Plant
import enums.Action
import enums.Direction
import enums.PlantType
import enums.TileType
import kotlin.math.abs

data class Tile(private val id: Int, private var type: TileType?, internal val coordinates: Coordinate, private val airflow: Boolean, private val direction: Direction?, private var farmId: Int, private val shed: Boolean, private val possiblePlant: List<PlantType>?, private var plant: Plant?, private val dead: Boolean, private var fallowPeriodOver: Int, private val env: Environment?, private var harvestEstimate: Int = 0) {

    fun getId() = id
    fun getType() = type
    fun getCoordinates() = coordinates
    fun getAirflow() = airflow
    fun getDirection() = direction
    fun getFarmId() = farmId
    fun getShed() = shed
    fun getPlant() = plant
    fun getEnvironment() = env
    fun getHarvestEstimate() = harvestEstimate

    fun isSquare(): Boolean {
        return abs(coordinates.getX() % 2) == 1
    }

    fun needsAction(action: Action, currentTick: Int): Boolean = TODO()

    fun setHarvestEstimate(newEstimate: Int) {
        harvestEstimate = newEstimate
    }

    fun setPlant(newPlant: Plant) {
        plant = newPlant
    }
}