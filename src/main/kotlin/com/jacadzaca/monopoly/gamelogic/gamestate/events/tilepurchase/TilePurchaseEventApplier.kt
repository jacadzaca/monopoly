package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult.VerifiedTilePurchaseEvent

class TilePurchaseEventApplier : GameEventApplier<VerifiedTilePurchaseEvent> {
  override fun apply(event: VerifiedTilePurchaseEvent, gameState: GameState): GameState {
    return gameState
      .update(event.tileIndex, event.tile.changeOwner(event.buyerId))
      .update(event.buyerId, event.buyer.detractFunds(event.tile.price))
  }
}
