package com.jacadzaca.monopoly.gamelogic.player

internal class DiceRollPlayerMover(private val dieRoller: () -> (Int)) : PlayerMover {
  override fun move(toMove: Player, boardSize: Int): Player {
    return toMove.copy(position = (toMove.position + dieRoller()) % boardSize)
  }
}
