package environmental

import Constants
import kotlin.math.max

class Environment(private var currentSoilMoisture: Int, private var capacity: Int) {
    private var sunlightPenalty: Int = 0
    private var beeHappyEvents: Double = 1.0
    private var animalAttack: Boolean = false

    fun getCurrentSoilMoisture() = currentSoilMoisture
    fun getCapacity() = capacity
    fun getSunlightPenalty() = sunlightPenalty
    fun getBeeHappyEvents() = beeHappyEvents
    fun getAnimalAttack() = animalAttack

    fun setSunlightPenalty(newPenalty: Int) {
        sunlightPenalty = newPenalty
    }

    fun setBeeHappyEvents(newBeeHappy: Double) {
        beeHappyEvents = newBeeHappy
    }

    fun setAnimalAttack(attack: Boolean) {
        animalAttack = attack
    }

    fun updateSoil(hasPlant: Boolean, threshold: Int): Boolean {
        currentSoilMoisture = if (hasPlant) max(currentSoilMoisture - Constants.SOIL_REDUCTION_PLANT, Constants.NO_VALUE)
        else max(currentSoilMoisture - Constants.SOIL_REDUCTION_NO_PLANT, Constants.NO_VALUE)
        return currentSoilMoisture >= threshold
    }
}