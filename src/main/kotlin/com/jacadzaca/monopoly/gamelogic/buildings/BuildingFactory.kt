package com.jacadzaca.monopoly.gamelogic.buildings

/**
 * https://en.wikipedia.org/wiki/Flyweight_pattern
 * This class is supposed to manage the reusable building objects
 */
interface BuildingFactory {
  companion object {
    val standardBuildingFactory: BuildingFactory = StandardBuildingFactory()
  }
  fun create(type: BuildingType): Building
}
