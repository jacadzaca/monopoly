package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.DeltaCalculator
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger

class TileDeltaCalculator : DeltaCalculator<Tile> {
  override fun calculate(current: Tile, previous: Tile): TileDelta {
    return TileDelta(
      calculateChangeInBuildings(current.estates, previous.estates),
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
      current: PersistentList<Estate>,
      previous: PersistentList<Estate>
  ): List<Estate>? {
    return when (current) {
      previous -> null
      else -> current
    }
  }
}
