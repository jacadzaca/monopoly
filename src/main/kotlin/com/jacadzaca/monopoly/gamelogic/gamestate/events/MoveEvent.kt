package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import java.util.*

data class MoveEvent(override val playerId: UUID) :
  GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState =
    gameStateManager.applyEvent(this, gameState)
}
