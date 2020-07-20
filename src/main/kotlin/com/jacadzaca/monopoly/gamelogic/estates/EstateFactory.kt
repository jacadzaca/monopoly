package com.jacadzaca.monopoly.gamelogic.estates

import java.math.BigInteger

/**
 * https://en.wikipedia.org/wiki/Flyweight_pattern
 * This class is supposed to manage the reusable building objects
 */
interface EstateFactory {
  companion object {
    val standardEstateFactory: EstateFactory = StandardEstateFactory()
  }
  fun create(type: EstateType): Estate
  fun getPriceFor(type: EstateType): BigInteger
}
