package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

internal interface RentCalculator {
  fun getTotalRentFor(tile: Tile): BigInteger
}
