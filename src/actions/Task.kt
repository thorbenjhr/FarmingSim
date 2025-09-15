package actions

import enums.Action
import harvesting.Plant

data class Task(private val id: Int, private val action: Action, private val plant: Plant?, private val tileId: Int, private val currentTick: Int, private var assignedMachine: Machine?, private var shedId: Int)