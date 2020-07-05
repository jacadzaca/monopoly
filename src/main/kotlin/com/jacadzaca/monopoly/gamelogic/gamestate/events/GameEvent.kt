package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

interface GameEvent {
  val playerId: PlayerID

  fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState
}
