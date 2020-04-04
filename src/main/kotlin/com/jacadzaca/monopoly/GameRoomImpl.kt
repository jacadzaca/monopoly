package com.jacadzaca.monopoly

import java.util.Queue

class GameRoomImpl(private val players: Queue<NetworkPlayer>,
                   private val game: MonopolyLogic,
                   private val inputAllower: InputAllowerImpl) : GameRoom<NetworkPlayer> {
  override fun executeAction(action: String): String {
    val parsedAction = action.split(" ")
    val currentPlayer = players.peek()
    val report: NetworkPlayer;
    if (parsedAction[0] == "move") {
      val moveSize = parsedAction[1].toInt()
      report = currentPlayer.copy(piece = game.movePiece(moveSize, currentPlayer.piece))
      nextTurn(report)
      return report.piece.toString()
    }
    return "Wrong input"
  }

  internal fun notYourTurnSupplier(): String {
    return "not your turn"
  }

  override fun addPlayer(player: NetworkPlayer) {
    if (players.isEmpty()) {
      inputAllower.allowInput(player, this::executeAction)
    } else {
      inputAllower.disallowInput(player, this::notYourTurnSupplier)
    }
    players.add(player)
  }

  private fun nextTurn(currentPlayer: NetworkPlayer) {
    players.remove()
    players.add(currentPlayer)
  }
}
