package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.DeltaCalculator
import java.lang.IllegalArgumentException
import java.math.BigInteger

class PlayerDeltaCalculator(private val boardSize: Int) : DeltaCalculator<Player> {
  override fun calculate(current: Player, previous: Player): PlayerDelta {
    if (current.id != previous.id) {
      throw IllegalArgumentException("Cannot create a delta from two different players!")
    }
    return PlayerDelta(
      calculateChangeInPosition(current.position, previous.position),
      calculateChangeInBalance(current.balance, previous.balance),
      calculateChangeInLiability(current.liability, previous.liability)
    )
  }

  /**
   * Keep in mind, that NO change in position is interpreted as traversing
   * the whole board. We never not move, every turn begins
   * with a player moving, so it's ok.
   */
  private fun calculateChangeInPosition(current: Int, previous: Int): Int {
    return when {
      current < previous -> boardSize - (previous - current)
      current == previous -> boardSize
      else -> current - previous
    }
  }

  private fun calculateChangeInBalance(current: BigInteger, previous: BigInteger): BigInteger? {
    return current - previous
  }

  private fun calculateChangeInLiability(current: Liability?, previous: Liability?): Liability? {
    return when {
      current != null && previous == null -> current
      else -> null
    }
  }
}
