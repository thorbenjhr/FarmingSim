package layout

import kotlin.math.abs

data class Coordinate(private val x: Int, private val y: Int) {

    fun getX() = x
    fun getY() = y

    private fun getNeighbours(): List<Coordinate> {
        val n = if (abs(x % 2) == 1) { // square
            listOf(
                Pair(-1, -1), Pair(1, -1), Pair(-1, 1), Pair(1, 1)
            )
        } else { // octagonal
            listOf(
                Pair(0, -2), Pair(1, -1), Pair(2, 0), Pair(1, 1), Pair(0, 2), Pair(-1 ,1), Pair(-2, 0), Pair(-1, -1)
            )
        }

        // has neighbours and exist
        return n.map { (dx, dy) -> (Coordinate(x + dx, y + dy)) }
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