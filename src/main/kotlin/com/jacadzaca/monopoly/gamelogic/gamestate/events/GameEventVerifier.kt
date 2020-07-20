package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

interface GameEventVerifier {
  fun verifyTilePurchaseEvent(event: TilePurchaseEvent, gameState: GameState): TilePurchaseEvent?
  fun verifyEstatePurchaseEvent(event: PropertyPurchaseEvent, gameState: GameState): PropertyPurchaseEvent?
}
