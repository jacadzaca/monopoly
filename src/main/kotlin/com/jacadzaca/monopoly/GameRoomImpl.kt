package com.jacadzaca.monopoly

import java.util.Queue

class GameRoomImpl(private val players: Queue<Player>, private val game: MonopolyLogic) : GameRoom {
  override fun executeAction(action: String): String {
    val parsedAction = action.split(" ")
    if (parsedAction[0] == "move") {
      return game.movePiece(parsedAction[1].toInt(), players.peek().piece).toString()
    }
    return "Wrong input"
  }
}
