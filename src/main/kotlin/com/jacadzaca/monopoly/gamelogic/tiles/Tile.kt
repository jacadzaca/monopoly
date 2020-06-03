package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.Building
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger
import java.util.*

data class Tile(val buildings: PersistentList<Building>,
                val price: BigInteger,
                var owner: UUID?) {

  fun totalRent(): BigInteger {
    return buildings.map(Building::rent).reduce(BigInteger::add)
  }
}
