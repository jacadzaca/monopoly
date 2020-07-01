package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger
import java.util.*

data class Tile(val buildings: PersistentList<Building>,
                val price: BigInteger,
                val owner: PlayerID?) {

  fun totalRent(): BigInteger {
    return buildings.map(Building::rent).reduce(BigInteger::add)
  }

  fun houseCount(): Int {
    return buildings.filter { it.buildingType == BuildingType.HOUSE }.count()
  }
}
