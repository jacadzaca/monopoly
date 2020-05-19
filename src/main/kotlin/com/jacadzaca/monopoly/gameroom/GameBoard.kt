package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.Player

interface GameBoard {
  companion object {
    private val standardGameBoard = GameBoardImpl(20, (1..6)::random)

    @JvmStatic
    fun create(): GameBoard = standardGameBoard
  }
  /**
   * pretends to roll a dice and then moves the @param player
   */
  fun movePlayer(player: Player): Player
}
