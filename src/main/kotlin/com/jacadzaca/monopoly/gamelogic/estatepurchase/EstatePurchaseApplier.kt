package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.VerificationResult.VerifiedEstatePurchaseEvent

internal class EstatePurchaseApplier(private val estateFactory: EstateFactory) :
  GameEventApplier<VerifiedEstatePurchaseEvent> {
  override fun apply(event: VerifiedEstatePurchaseEvent, gameState: GameState): GameState {
    return gameState
      .update(event.tileIndex, event.tile.addEstate(estateFactory.create(event.estateType)))
      .update(event.buyerId, event.buyer.detractFunds(estateFactory.getPriceFor(event.estateType)))
  }
}
