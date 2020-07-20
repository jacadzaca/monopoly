package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.gamestate.events.*
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.PropertyPurchaseEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEvent

interface GameStateManager {
  /**
   * applies given @event to the @gameState, while adding the change to @gameState.recentChanges
   */
  fun applyEvent(event: GameEvent, gameState: GameState): GameState {
    return event.apply(this, gameState.addChange(event))
  }

  fun applyPlayerMove(event: MoveEvent, gameState: GameState): GameState

  /**
   * @throws IllegalArgumentException if @event.playerId cannot buy the tile
   */
  fun applyTilePurchase(event: TilePurchaseEvent, gameState: GameState): GameState
  /**
   * @throws IllegalArgumentException if @event.playerId cannot buy the tile
   */
  fun applyEstatePurchase(event: PropertyPurchaseEvent, gameState: GameState): GameState

  /**
   * If the event.playerId has insufficient balance to pay his liabilities, ONLY the amount he has is to be
   * transferred to the liability receiver. The player, who cannot pay his debt,
   * should have his balance to be set to a non-positive value
   */
  fun applyLiabilityPayment(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState
}
