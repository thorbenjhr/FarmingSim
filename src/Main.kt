import actions.Bfs
import actions.FarmHandler
import enums.TileType
import environmental.CloudManager
import harvesting.HarvestEstimator
import incidents.IncidentManager
import layout.Coordinate
import layout.MapClass
import layout.Tile
import logging.Logger

fun main() {
    test1()
    test2()

    Logger("INFO", "stdout")
    val h = HarvestEstimator()
    h.logSimulationStatistics()

    val sim = Simulation(MapClass(emptyMap(), emptyMap()), emptyMap(), FarmHandler(Bfs(MapClass(emptyMap(), emptyMap()))), CloudManager(
        MapClass(emptyMap(), emptyMap()), mutableMapOf()), HarvestEstimator(), IncidentManager(emptyMap()), 1, 30)

    sim.start()

    /*val n = Coordinate(5, 5).getNeighbours(2)
    println("Size: ${n.size}")
    n.forEach { c -> println("Coordinate ${c.getX()}, ${c.getY()}") }
     */
}

fun test2() {
    val t1 = Tile(1, TileType.FIELD, Coordinate(0, 0), false, null,  5, false, null, null, false, 0, null, 0)
    val t2 = Tile(2, TileType.VILLAGE, Coordinate(2, 0), false, null,-1, false, null, null,  false, 0,null, 0)
    val t3 = Tile(3, TileType.ROAD, Coordinate(-1, 1), false, null, -1, false, null, null, false, 0, null, 0)
    val t4 = Tile(4, TileType.MEADOW, Coordinate(1, 1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t5 = Tile(5, TileType.MEADOW, Coordinate(3, 1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t6 = Tile(6, TileType.FIELD, Coordinate(0, 2), false, null,5, false, null, null,  false, 0,null, 0)
    val t7 = Tile(7, TileType.FIELD, Coordinate(2, 2), false, null,5, false, null, null,  false, 0,null, 0)
    val t8 = Tile(8, TileType.ROAD, Coordinate(-1, 3), false, null,-1, false, null, null,  false, 0,null, 0)
    val t9 = Tile(9, TileType.FARMSTEAD, Coordinate(1, 3), false, null,5, true, null, null,  false, 0,null, 0)
    val t10 = Tile(10, TileType.ROAD, Coordinate(3, 3), false, null,-1, false, null, null,  false, 0,null, 0)
    val t11 = Tile(11, null, Coordinate(-1, -1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t12 = Tile(12, null, Coordinate(1, -1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t13 = Tile(13, TileType.FIELD, Coordinate(3, -1), false, null,5, false, null, null,  false, 0,null, 0)

    val tilesById = mapOf(
        1 to t1,
        2 to t2,
        3 to t3,
        4 to t4,
        5 to t5,
        6 to t6,
        7 to t7,
        8 to t8,
        9 to t9,
        10 to t10,
        11 to t11,
        12 to t12,
        13 to t13,
    )

    val tilesByCoord = mapOf(
        t1.coordinates to t1,
        t2.coordinates to t2,
        t3.coordinates to t3,
        t4.coordinates to t4,
        t5.coordinates to t5,
        t6.coordinates to t6,
        t7.coordinates to t7,
        t8.coordinates to t8,
        t9.coordinates to t9,
        t10.coordinates to t10,
        t11.coordinates to t11,
        t12.coordinates to t12,
        t13.coordinates to t13,
    )

    val mapMap = MapClass(tilesById, tilesByCoord)
    val bfs = Bfs(mapMap)

    // BFS from id 9 to id 13 no harvest expect true
    val p1 = bfs.findPath(9, 13, 5, false)
    println("Path from id 9 to id 13 without harvest: $p1")

    // BFS from id 9 to id 13 with harvest expect false
    val p2 = bfs.findPath(9, 13, 5, true)
    println("Path from id 9 to id 13 with harvest: $p2")
}

fun test1() {
    val t1 = Tile(1, TileType.FIELD, Coordinate(0, 0), false, null,  5, false, null, null, false, 0, null, 0)
    val t2 = Tile(2, TileType.VILLAGE, Coordinate(2, 0), false, null,-1, false, null, null,  false, 0,null, 0)
    val t3 = Tile(3, TileType.ROAD, Coordinate(-1, 1), false, null, -1, false, null, null, false, 0, null, 0)
    val t4 = Tile(4, TileType.MEADOW, Coordinate(1, 1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t5 = Tile(5, TileType.MEADOW, Coordinate(3, 1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t6 = Tile(6, TileType.FIELD, Coordinate(0, 2), false, null,5, false, null, null,  false, 0,null, 0)
    val t7 = Tile(7, TileType.FIELD, Coordinate(2, 2), false, null,5, false, null, null,  false, 0,null, 0)
    val t8 = Tile(8, TileType.ROAD, Coordinate(-1, 3), false, null,-1, false, null, null,  false, 0,null, 0)
    val t9 = Tile(9, TileType.FARMSTEAD, Coordinate(1, 3), false, null,5, true, null, null,  false, 0,null, 0)
    val t10 = Tile(10, TileType.ROAD, Coordinate(3, 3), false, null,-1, false, null, null,  false, 0,null, 0)
    val t11 = Tile(11, null, Coordinate(-1, -1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t12 = Tile(12, null, Coordinate(1, -1), false, null,-1, false, null, null,  false, 0,null, 0)
    val t13 = Tile(13, null, Coordinate(3, -1), false, null,-1, false, null, null,  false, 0,null, 0)

    val tilesById = mapOf(
        1 to t1,
        2 to t2,
        3 to t3,
        4 to t4,
        5 to t5,
        6 to t6,
        7 to t7,
        8 to t8,
        9 to t9,
        10 to t10,
        11 to t11,
        12 to t12,
        13 to t13,
    )

    val tilesByCoord = mapOf(
        t1.coordinates to t1,
        t2.coordinates to t2,
        t3.coordinates to t3,
        t4.coordinates to t4,
        t5.coordinates to t5,
        t6.coordinates to t6,
        t7.coordinates to t7,
        t8.coordinates to t8,
        t9.coordinates to t9,
        t10.coordinates to t10,
        t11.coordinates to t11,
        t12.coordinates to t12,
        t13.coordinates to t13,
    )

    val mapMap = MapClass(tilesById, tilesByCoord)
    val bfs = Bfs(mapMap)
    // BFS from id 9 to id 1 expect true
    val p1 = bfs.findPath(9, 1, 5, false)
    println("Path from id 9 to id 1: $p1")

    // BFS from id 9 to id 2 expect true
    val p2 = bfs.findPath(9, 2, 5, false)
    println("Path from id 9 to id 2: $p2")

    // BFS from id 9 to id 13 expect false
    val p3 = bfs.findPath(9, 13, 5, false)
    println("Path from id 9 to id 13: $p3")

    // BFS from id 10 to id 1 carrying harvest expect true
    val p4 = bfs.findPath(10, 1, 5, true)
    println("Path from id 10 to id 1: $p4")
}