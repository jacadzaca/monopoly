package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean {
    return player.id == event.committerId
  }

  override fun movePlayer(player: Player): Player {
    return player.copy(piece = Piece(wrapMove(player.piece.position, dieRoller())))
  }

  override fun addFunds(player: Player, howMuch: BigInteger): Player {
    return player.copy(balance = player.balance + howMuch)
  }

  override fun detractFunds(player: Player, howMuch: BigInteger): Player {
    if (player.balance < howMuch) {
      throw IllegalArgumentException("$player has insufficient funds")
    }
    return player.copy(balance = player.balance - howMuch)
  }

  private fun wrapMove(currentPosition: Int, move: Int): Int {
    return (currentPosition + move) % boardSize
  }

}
