package harvesting

import enums.Action
import enums.PlantType

data class Plant(private var lastActions: Map<Int, Action>, private var type: PlantType)