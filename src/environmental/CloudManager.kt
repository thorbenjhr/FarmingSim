package environmental

import layout.MapClass
import kotlin.math.min

class CloudManager(private var map: MapClass, private var clouds: MutableMap<Int, Cloud>) {
    private var tileToCloud: MutableMap<Int, Int> = mutableMapOf()

    fun applyMoves() {
        clouds.keys.forEach { id -> computeNextLocations(id) }
    }

    private fun computeNextLocations(id: Int) {
        TODO()
    }

    private fun applyRain(id: Int) {
        TODO()
    }

    private fun decrementAndDissipate(id: Int) {
        TODO()
    }

    private fun applyMerge(onTileId: Int, movingId: Int, movesLeft: Int): Pair<Int, Cloud> {
        val cloudOnTile = clouds[onTileId]
        val cloudMoving = clouds[movingId]
        val c = Cloud(clouds.keys.max() + 1, cloudOnTile!!.getLocation(), min(cloudOnTile.getDuration(), cloudMoving!!.getDuration()), cloudOnTile.getAmount() + cloudMoving.getAmount())
        // fix how many moves it can make
        return Pair(0, c)
    }

    fun addCloud(cloud: Cloud) {
        cloud.setId(clouds.keys.max() + 1)
        clouds[cloud.getId()] = cloud
    }
}