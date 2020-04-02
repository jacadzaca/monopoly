package com.jacadzaca.monopoly

interface GameRoom {
  fun executeAction(action: String): String
  fun addPlayer(player: Player)
}
