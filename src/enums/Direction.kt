package enums

enum class Direction(val dx: Int, val dy: Int, val square: Boolean) {
    NORTH(0, -2, false),
    NORTHEAST(1, -1, true),
    EAST(2, 0, false),
    SOUTHEAST(1,1, true),
    SOUTH(0,2, false),
    SOUTHWEST(-1,1, true),
    WEST(-2,0, false),
    NORTHWEST(-1,-1, true)
}