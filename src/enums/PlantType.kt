package enums

import harvesting.PlantSpec

enum class PlantType(private val spec: PlantSpec) {
    OAT(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    PUMPKIN(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    POTATO(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    WHEAT(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    APPLE(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    ALMOND(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    CHERRY(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap())),
    GRAPE(PlantSpec(0,0,0,emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), false, emptyMap()));

    fun getSpec() = spec
}