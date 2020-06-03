package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.Building
import java.math.BigInteger

internal class RentCalculatorImpl : RentCalculator {
  override fun getTotalRentFor(tile: Tile): BigInteger {
    return tile.buildings.map(Building::rent).reduce(BigInteger::add)
  }
}
