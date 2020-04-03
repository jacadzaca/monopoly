package com.jacadzaca.monopoly

interface GameRoom<T : Player> {
  fun executeAction(action: String): String
  fun addPlayer(player: T)
}
