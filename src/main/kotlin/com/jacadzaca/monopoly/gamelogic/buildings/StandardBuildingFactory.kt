package com.jacadzaca.monopoly.gamelogic.buildings

import java.math.BigInteger

internal class StandardBuildingFactory : BuildingFactory {
  private companion object {
    val house = Building(100.toBigInteger())
    val hotel = Building(1000.toBigInteger())
  }

  override fun create(type: BuildingType): Building {
    return when(type) {
      BuildingType.HOUSE -> house
      BuildingType.HOTEL -> hotel
    }
  }

  override fun getPriceFor(type: BuildingType): BigInteger {
    return when(type) {
      BuildingType.HOUSE -> 500.toBigInteger()
      BuildingType.HOTEL -> 2000.toBigInteger()
    }
  }
}
