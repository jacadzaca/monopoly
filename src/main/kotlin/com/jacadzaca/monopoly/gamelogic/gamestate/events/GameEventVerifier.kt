package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

typealias VerifiedPropertyPurchaseEvent = PropertyPurchaseEvent
typealias VerifiedTilePurchaseEvent = TilePurchaseEvent

interface GameEventVerifier {
  fun verifyTilePurchaseEvent(event: TilePurchaseEvent, gameState: GameState): VerifiedTilePurchaseEvent?
  fun verifyEstatePurchaseEvent(event: PropertyPurchaseEvent, gameState: GameState): VerifiedPropertyPurchaseEvent?
}
