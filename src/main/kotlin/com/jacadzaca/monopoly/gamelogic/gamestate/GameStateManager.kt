package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PlayerPaysLiabilityEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PropertyPurchaseEvent

interface GameStateManager {
  fun applyEvent(event: MoveEvent, gameState: GameState): GameState
  fun applyEvent(event: PropertyPurchaseEvent, gameState: GameState): GameState

  /**
   * If the event.playerId has insufficient balance to pay his liabilities, ONLY the amount he has is to be
   * transferred to the liability receiver. The player, who cannot pay his debt,
   * should have his balance to be set to a non-positive value
   */
  fun applyEvent(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState
}
