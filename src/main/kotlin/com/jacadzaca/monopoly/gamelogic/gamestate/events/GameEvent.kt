package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import java.util.*

interface GameEvent {
  val playerId: UUID

  fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState
}
