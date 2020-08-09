package com.jacadzaca.monopoly.gamelogic.estates

import java.math.BigInteger

sealed class Estate(open val rent: BigInteger) {
  data class House(override val rent: BigInteger): Estate(rent)
  data class Hotel(override val rent: BigInteger): Estate(rent)
}
