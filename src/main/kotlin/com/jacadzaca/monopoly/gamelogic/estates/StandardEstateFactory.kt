package com.jacadzaca.monopoly.gamelogic.estates

import java.math.BigInteger

internal class StandardEstateFactory : EstateFactory {
  private companion object {
    val house = Estate(100.toBigInteger(), EstateType.HOUSE)
    val hotel = Estate(1000.toBigInteger(), EstateType.HOTEL)
  }

  override fun create(type: EstateType): Estate {
    return when(type) {
      EstateType.HOUSE -> house
      EstateType.HOTEL -> hotel
    }
  }

  override fun getPriceFor(type: EstateType): BigInteger {
    return when(type) {
      EstateType.HOUSE -> 500.toBigInteger()
      EstateType.HOTEL -> 2000.toBigInteger()
    }
  }
}
