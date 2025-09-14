package enums

import harvesting.PlantSpec

enum class PlantType(private val spec: PlantSpec) {
    OAT(PlantSpec()),
    PUMPKIN(PlantSpec()),
    POTATO(PlantSpec()),
    WHEAT(PlantSpec()),
    APPLE(PlantSpec()),
    ALMOND(PlantSpec()),
    CHERRY(PlantSpec()),
    GRAPE(PlantSpec());

    fun getSpec() = spec
}