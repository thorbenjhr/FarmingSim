package harvesting

import enums.Action
import enums.PlantType

data class Plant(private var lastActions: MutableMap<Int, Action>, private var type: PlantType) {
    fun getLastActions() = lastActions
    fun getType() = type
    fun setLastActions(lastActionTick: Int, lastAction: Action) {
        if (lastActions.containsKey(lastActionTick)) {
            lastActions.remove(lastActionTick)
        }
        lastActions[lastActionTick] = lastAction
    }
}