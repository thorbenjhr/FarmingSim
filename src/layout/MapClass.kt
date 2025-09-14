package layout

import enums.Direction

class MapClass (private val tiles: Map<Int, Tile>, private val tilesCoordinates: Map<Coordinate, Tile>) {
    fun getTileByIndex(idx: Int): Tile? {
        return tiles[idx]
    }

    fun getTileByCoordinates(c: Coordinate): Tile? {
        return tilesCoordinates[c]
    }

    fun getTileByIdAndDirection(idx: Int, direction: Direction): Tile? {
        TODO()
    }
}