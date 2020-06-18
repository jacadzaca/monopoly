package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PropertyPurchaseEvent

interface GameStateManager {
  fun applyEvent(event: MoveEvent, gameState: GameState): GameState
  fun applyEvent(event: PropertyPurchaseEvent, gameState: GameState): GameState
}
