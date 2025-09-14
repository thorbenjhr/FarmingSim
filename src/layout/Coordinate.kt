package layout

import enums.Direction
import kotlin.math.abs

data class Coordinate(private val x: Int, private val y: Int) {

    fun getX() = x
    fun getY() = y

    private fun getNeighbours(): List<Coordinate> {
        val n = if (abs(x % 2) == 1) { // square
            Direction.entries.filter { it.square }
        } else { // octagonal
            Direction.entries
        }
        return n.map { dir -> Coordinate(x + dir.dx, y + dir.dy) }
    }

    fun getNeighbours(radius: Int = 1): List<Coordinate> {
        val visited = mutableSetOf<Coordinate>()
        var currDepth = listOf(this)

        repeat(radius) {
            val nextDepth = mutableListOf<Coordinate>()
            for (c in currDepth) {
                for (n in c.getNeighbours()) {
                    if (n !in visited) {
                        visited.add(n)
                        nextDepth.add(n)
                    }
                }
            }
            currDepth = nextDepth
        }
        return visited.toList()
    }
}