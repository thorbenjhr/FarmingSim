package incidents

import Constants
import actions.Farm
import enums.IncidentType
import enums.PlantType
import enums.TileType
import environmental.Cloud
import environmental.CloudManager
import layout.MapClass
import layout.Tile
import kotlin.math.max

class IncidentManager(private val incidents: Map<Int, List<Incident>>) {
    fun handleIncidents(tick: Int, cm: CloudManager, m: MapClass, farms: Map<Int, Farm>) {
        val tickIncidents = incidents[tick]?: return

        for (incident in tickIncidents) {
            when (incident.getType()) {
                IncidentType.CLOUDCREATION -> applyCloudCreation(incident, cm)
                IncidentType.BEEHAPPY -> applyBeeHappy()
                IncidentType.DROUGHT -> applyDrought(incident, m, farms, tick)
                IncidentType.BROKENMACHNINE -> applyBrokenMachine(incident, farms, tick)
                IncidentType.CITYEXPANSION -> applyCityExpansion(incident, m, farms)
                IncidentType.ANIMALATTACK -> applyAnimalAttack(incident, m)
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

    private fun applyDrought(incident: Incident, m: MapClass, farms: Map<Int, Farm>, tick: Int) {
        val tile = m.getTileByIndex(incident.getLocation())
        val coord = tile?.getCoordinates()
        val neighboursCoord = coord?.getNeighbours(incident.getRadius())
        val affectedTiles = neighboursCoord?.distinct()?.mapNotNull { m.getTileByCoordinates(it) }?.filter { it.getType() == TileType.PLANTATION || it.getType() == TileType.FIELD }
        affectedTiles?.forEach { t ->
            t.getEnvironment()?.setSoilMoisture(Constants.NO_VALUE)
            t.setHarvestEstimate(Constants.NO_VALUE)
            t.setPlant(null)
            if (t.getType() == TileType.PLANTATION) {
                t.setDead(true)
                farms[t.getFarmId()]?.getPlantations()?.remove(t.getId())
            } else {
                t.setFallowPeriodOver(tick + Constants.FALLOW_PERIOD_TICKS + 1)
            }
        }
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

    fun applyCityExpansion(incident: Incident, m: MapClass, farms: Map<Int, Farm>) {
        val tile = m.getTileByIndex(incident.getLocation())?: return
        if (tile.getType() == TileType.FIELD) {
            farms[tile.getFarmId()]?.getFields()?.remove(tile.getId())
        }
        tile.setTileType(TileType.VILLAGE)
    }

    private fun applyAnimalAttack(incident: Incident, m: MapClass) {
        val tile = m.getTileByIndex(incident.getLocation())
        val coord = tile?.getCoordinates()
        val neighboursCoord = coord?.getNeighbours(incident.getRadius())

        val affectedForests = neighboursCoord?.distinct()?.mapNotNull { m.getTileByCoordinates(it) }?.filter { it.getType() == TileType.FOREST }.orEmpty()
        val affectedNeighbours = affectedForests.flatMap { forest -> forest.getCoordinates().getNeighbours() }.mapNotNull { m.getTileByCoordinates(it) }.filter { it.getType() == TileType.FIELD || it.getType() == TileType.PLANTATION }.distinct()
        affectedNeighbours.forEach { t ->
            if (t.getType() == TileType.FIELD || (t.getType() == TileType.PLANTATION && t.getPlant()?.getType() == PlantType.GRAPE)) {
                t.setHarvestEstimate(t.getHarvestEstimate() / 2)
            } else {
                // RESET MOWING!!!
                t.setHarvestEstimate((t.getHarvestEstimate() * 0.9).toInt())
            }
        }
    }
}