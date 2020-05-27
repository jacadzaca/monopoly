package com.jacadzaca.monopoly.gamelogic

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, action: GameAction): Boolean {
    return player.id == action.committerId
  }

  override fun movePlayer(player: Player): Player {
    return player.copy(piece = Piece(wrapMove(player.piece.position, dieRoller())))
  }

  private fun wrapMove(currentPosition: Int, move: Int): Int {
    return (currentPosition + move) % boardSize
  }

}
