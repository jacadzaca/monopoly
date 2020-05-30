package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

interface RentCalculator {
  fun getTotalRentFor(field: Field): BigInteger
}
