package parser

import actions.Farm
import environmental.Cloud
import incidents.Incident
import layout.Tile

class Accumulator {
    private var tiles = mapOf<Int, Tile>()
    private var farms = mapOf<Int, Farm>()
    private var clouds = mapOf<Int, Cloud>()
    private var incidents = mapOf<Int, Incident>()

    fun getTiles() = tiles
    fun getFarms() = farms
    fun getClouds() = clouds
    fun getIncidents() = incidents

    fun setTiles(newTiles: Map<Int, Tile>) {
        tiles = newTiles
    }

    fun setFarms(newFarms: Map<Int, Farm>) {
        farms = newFarms
    }

    fun setClouds(newClouds: Map<Int, Cloud>) {
        clouds = newClouds
    }

    fun setIncidents(newIncidents: Map<Int, Incident>) {
        incidents = newIncidents
    }
}