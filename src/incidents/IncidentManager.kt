package incidents

import Constants
import actions.Farm
import enums.IncidentType
import environmental.Cloud
import environmental.CloudManager
import layout.MapClass
import kotlin.math.max

class IncidentManager(private val incidents: Map<Int, List<Incident>>) {
    fun handleIncidents(tick: Int, cm: CloudManager, m: MapClass, farms: Map<Int, Farm>) {
        val tickIncidents = incidents[tick]?: return

        for (incident in tickIncidents) {
            when (incident.getType()) {
                IncidentType.CLOUDCREATION -> applyCloudCreation(incident, cm)
                IncidentType.BEEHAPPY -> applyBeeHappy()
                IncidentType.DROUGHT -> applyDrought()
                IncidentType.BROKENMACHNINE -> applyBrokenMachine(incident, farms, tick)
                IncidentType.CITYEXPANSION -> applyCityExpansion(incident, m, farms)
                IncidentType.ANIMALATTACK -> applyAnimalAttack()
            }
        }
    }

    private fun applyCloudCreation(incident: Incident, cm: CloudManager) {
        val cloud = Cloud(Constants.NO_VALUE, incident.getLocation(), incident.getDuration(), incident.getAmount())
        cm.addCloud(cloud)
    }

    private fun applyBeeHappy() {
        TODO()
    }

    private fun applyDrought() {
        TODO()
    }

    private fun applyBrokenMachine(incident: Incident, farms: Map<Int, Farm>, tick: Int) {
        for (farm in farms.values) {
            val machine = farm.getMachines()[incident.getMachineId()]
            if (machine?.getBrokenIncidentOver() == -1) {
                return
            } else {
                machine?.setBrokenIncidentOver(max(machine.getBrokenIncidentOver(), tick + incident.getDuration() + 1))
            }
            break
        }
    }

    private fun applyCityExpansion(incident: Incident, m: MapClass, farms: Map<Int, Farm>) {
        TODO()
    }

    private fun applyAnimalAttack() {
        TODO()
    }
}