package com.jacadzaca.monopoly.gamelogic.player

internal class DiceRollPlayerMover(private val dieRoller: () -> (Int)) : PlayerMover {
  override fun move(toMove: Player, boardSize: Int): Player {
    return toMove.copy(position = wrap(toMove.position + dieRoller(), boardSize))
  }

  private fun wrap(toWrap: Int, max: Int): Int {
    return toWrap % max
  }
}
