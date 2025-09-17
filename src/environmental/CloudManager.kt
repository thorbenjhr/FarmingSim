package environmental

import Constants
import enums.TileType
import layout.MapClass
import logging.Logger
import java.util.PriorityQueue
import kotlin.math.min

class CloudManager(private var map: MapClass, private var clouds: MutableMap<Int, Cloud>) {
    private var maxId = clouds.keys.max()
    private var tileToCloud: MutableMap<Int, Int> = clouds.values.associateBy({ it.getLocation() }, {it.getId()}).toMutableMap()
    private var newClouds: PriorityQueue<Pair<Int, Cloud>> = PriorityQueue(compareBy { it.second.getId() })

    fun applyMoves(yearTick: Int) {
        clouds.keys.forEach{ c -> computeNextLocations(c, Constants.MAX_MOVE_PER_TICK, yearTick) }
        newClouds.forEach{c -> computeNextLocations(c.second.getId(), c.first, yearTick)}
        newClouds.clear()
    }

    private fun computeNextLocations(id: Int, movesLeft: Int, yearTick: Int) {
        val cloud = clouds[id]
        val tile = map.getTileByIndex(cloud?.getLocation()!!)
        applyRain(id)

        for (move in movesLeft downTo 0) {
            val nextTile = map.getTileByIdAndDirection(cloud.getLocation(), tile?.getDirection()!!)
            if (nextTile?.getType() == TileType.VILLAGE) {
                TODO()
            }
            // fix rain amount
            Logger.logCloudMovement(cloud.getId(), 3, cloud.getLocation(), nextTile?.getId()!!)
            Logger.logCloudMovementSunlight(tile.getId(), Constants.SUNLIGHT_BASE_HOURS_PER_MONTH[yearTick]!! - tile.getEnvironment()?.getSunlightPenalty()!!)
        }
    }

    private fun applyRain(id: Int) {
        // TODO()
    }

    private fun decrementAndDissipate(id: Int) {
        val cloud = clouds[id]
        cloud?.setDuration(cloud.getDuration() - 1)
        if (cloud?.getDuration() == 0) {
            clouds.remove(id)
            tileToCloud.remove(cloud.getLocation())
            Logger.logCloudDissipation(id, cloud.getLocation())
        }
    }

    private fun applyMerge(onTileId: Int, movingId: Int, movesLeft: Int) {
        val cloudOnTile = clouds[onTileId]
        val cloudMoving = clouds[movingId]
        maxId += 1
        val c = Cloud(maxId, cloudOnTile!!.getLocation(), min(cloudOnTile.getDuration(), cloudMoving!!.getDuration()), cloudOnTile.getAmount() + cloudMoving.getAmount())
        val moves = min(movesLeft, Constants.MAX_MOVE_PER_TICK)
        newClouds.add(Pair(moves, c))
    }

    fun addCloud(cloud: Cloud) {
        maxId += 1
        cloud.setId(maxId)
        clouds[cloud.getId()] = cloud
        tileToCloud[cloud.getLocation()] = cloud.getId()
    }
}