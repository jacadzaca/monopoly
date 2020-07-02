package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.DeltaCalculator
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger

class TileDeltaCalculator : DeltaCalculator<Tile> {
  override fun calculate(current: Tile, previous: Tile): TileDelta {
    return TileDelta(
      calculateChangeInBuildings(current.buildings, previous.buildings),
      calculateChangeInPrice(current.price, previous.price),
      calculateChangeInOwner(current.owner, previous.owner)
    )
  }

  private fun calculateChangeInOwner(
    current: PlayerID?,
    previous: PlayerID?
  ): PlayerID? {
    return when (current) {
      previous -> null
      else -> current
    }
  }

  private fun calculateChangeInPrice(
    current: BigInteger,
    previous: BigInteger
  ): BigInteger? {
    return when (current) {
      previous -> null
      else -> current - previous
    }
  }

  private fun calculateChangeInBuildings(
    current: PersistentList<Building>,
    previous: PersistentList<Building>
  ): List<Building>? {
    return when (current) {
      previous -> null
      else -> current
    }
  }
}
