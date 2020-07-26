package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.VerificationResult.VerifiedTilePurchaseEvent

class TilePurchaseEventApplier :
  GameEventApplier<VerifiedTilePurchaseEvent> {
  override fun apply(event: VerifiedTilePurchaseEvent, gameState: GameState): GameState {
    return gameState
      .update(event.tileIndex, event.tile.changeOwner(event.buyerId))
      .update(event.buyerId, event.buyer.detractFunds(event.tile.price))
  }
}
