package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class MoveEvent(override val playerId: PlayerID) :
  GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState {
    return gameStateManager.applyPlayerMove(this, gameState)
  }
}
