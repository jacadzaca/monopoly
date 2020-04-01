package com.jacadzaca.monopoly

import java.util.Queue

class GameRoomImpl(private val players: Queue<Player>, private val game: MonopolyLogic) : GameRoom {
  override fun executeAction(action: String): String {
    val parsedAction = action.split(" ")
    val currentPlayer = players.peek()
    val report: Player;
    if (parsedAction[0] == "move") {
      val moveSize = parsedAction[1].toInt()
      report = currentPlayer.copyPlayer(piece = game.movePiece(moveSize, currentPlayer.piece))
      nextTurn(report)
      return report.piece.toString()
    }
    return "Wrong input"
  }

  private fun nextTurn(currentPlayer: Player) {
    players.remove()
    players.add(currentPlayer)
  }
}
