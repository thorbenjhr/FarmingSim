import enums.PlantType

object Constants {
    const val NO_VALUE = 0
    const val DEFAULT_NEIGHBOUR_DISTANCE = 2
    const val FALLOW_PERIOD_TICKS = 4
    const val MAX_MOVE_PER_TICK = 10
    const val SOIL_REDUCTION_NO_PLANT = 70
    const val SOIL_REDUCTION_PLANT = 100
    const val RAIN_THRESHOLD = 5000
    const val IMPORTANT = "IMPORTANT"
    const val DEBUG = "DEBUG"
    val ANIMAL_ATTACK_PENALTY: Map<PlantType, Double> = emptyMap()
    val SUNLIGHT_BASE_HOURS_PER_MONTH = mapOf(
        1 to 98,
        2 to 112,
        3 to 123,
        4 to 140,
        5 to 168,
        6 to 168,
        7 to 168,
        8 to 154,
        9 to 126,
        10 to 112,
        11 to 98,
        12 to 84
    )
}