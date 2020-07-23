package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

class EstatePurchaseApplier(private val estateFactory: EstateFactory) : GameEventApplier<VerificationResult.VerifiedEstatePurchaseEvent> {
  override fun apply(event: VerificationResult.VerifiedEstatePurchaseEvent, gameState: GameState): GameState {
    return gameState
      .update(event.tileIndex, event.tile.addEstate(estateFactory.create(event.estateType)))
      .update(event.playerId, event.player.detractFunds(estateFactory.getPriceFor(event.estateType)))
  }
}
