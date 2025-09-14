package actions

import Constants
import layout.MapClass
import layout.Tile
import enums.TileType

class Bfs (private val map: MapClass){

    fun findPath(startTileId: Int, endTileId: Int, farmId: Int, containsHarvest: Boolean): Boolean {
        val reachable = findAllReachableTiles(startTileId, farmId, containsHarvest)
        return endTileId in reachable
    }

    fun findTilesWithinDistance(startTileId: Int, maxDistance: Int = Constants.DEFAULT_NEIGHBOUR_DISTANCE, farmId: Int, containsHarvest: Boolean): Map<Int, Tile> {
        val startTile = map.getTileByIndex(startTileId)?: return emptyMap()
        val reachable = findAllReachableTiles(startTileId, farmId, containsHarvest)

        val neighboursInRadius = startTile.getCoordinates().getNeighbours(maxDistance).mapNotNull { map.getTileByCoordinates(it) }.filter { isTraversable(it, farmId, containsHarvest) }

        return neighboursInRadius.filter { it.getId() in reachable }.associateBy { it.getId() }
    }

    private fun findAllReachableTiles(startTileId: Int, farmId: Int, containsHarvest: Boolean): Set<Int> {
        val startTile = map.getTileByIndex(startTileId)?: return emptySet()

        val visited = mutableSetOf<Int>()
        val queue = ArrayDeque<Tile>()
        val reachableTiles = mutableSetOf<Int>()

        queue.add(startTile)
        visited.add(startTileId)

        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()

            if (curr.getType() in listOf(TileType.FARMSTEAD, TileType.FIELD, TileType.PLANTATION)) { // isTraversable(map.getTileByIndex(curr.getId())!!, farmId, containsHarvest) for all tile types
                reachableTiles.add(curr.getId())
            }

            val neighbours = curr.coordinates.getNeighbours().mapNotNull { map.getTileByCoordinates(it) }.filter { isTraversable(it, farmId, containsHarvest) }.sortedBy { it.getId() }

            for (neighbour in neighbours) {
                if (neighbour.getId() !in visited) {
                    visited.add(neighbour.getId())
                    queue.add(neighbour)
                }
            }
        }

        return reachableTiles
    }

    private fun isTraversable(tile: Tile, farmId: Int, containsHarvest: Boolean): Boolean {
        if (tile.getType() == null || tile.getType() == TileType.FOREST) return false
        if (tile.getType() == TileType.VILLAGE && containsHarvest) return false
        if (tile.getType() in listOf(TileType.FARMSTEAD, TileType.FIELD, TileType.PLANTATION)) return tile.getFarmId() == farmId
        return tile.getType() in listOf(TileType.ROAD, TileType.VILLAGE, TileType.MEADOW)
    }
}