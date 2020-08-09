package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

sealed class Estate(open val rent: BigInteger, open val price: BigInteger) {
  data class House(override val rent: BigInteger, override val price: BigInteger): Estate(rent, price)
  data class Hotel(override val rent: BigInteger, override val price: BigInteger): Estate(rent, price)
}
