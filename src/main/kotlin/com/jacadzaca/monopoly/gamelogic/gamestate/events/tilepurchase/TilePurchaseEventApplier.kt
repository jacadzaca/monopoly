package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

class TilePurchaseEventApplier : GameEventApplier<VerificationResult.VerifiedTilePurchaseEvent> {
  override fun apply(event: VerificationResult.VerifiedTilePurchaseEvent, gameState: GameState): GameState {
    return gameState
      .update(event.tileIndex, event.tile.copy(owner = event.playerId))
      .update(event.playerId, event.player.detractFunds(event.tile.price))
  }
}
