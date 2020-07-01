package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.DeltaCalculator
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import kotlinx.collections.immutable.PersistentList

class TileDeltaCalculator : DeltaCalculator<Tile> {
  override fun calculate(current: Tile, previous: Tile): TileDelta {
    return TileDelta(
      calculateChangeInBuildings(current.buildings, previous.buildings)
    )
  }

  private fun calculateChangeInBuildings(
    current: PersistentList<Building>,
    previous: PersistentList<Building>
  ): List<Building>? {
    val change = mutableListOf<Building>()
    for ((newBuilding, oldBuilding) in current zip previous) {
      if (newBuilding != oldBuilding) {
        change.add(newBuilding)
      }
    }
    return when {
        change.isEmpty() -> null
        else -> change
    }
  }
}
