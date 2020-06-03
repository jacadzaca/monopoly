package com.jacadzaca.monopoly.gamelogic.tiles

import java.math.BigInteger

internal interface RentCalculator {
  fun getTotalRentFor(tile: Tile): BigInteger
}
