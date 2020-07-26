package com.jacadzaca.monopoly.gamelogic.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventApplier

internal class PlayerPaysLiabilityEventApplier :
  GameEventApplier<PlayerPaysLiabilityEvent> {
  override fun apply(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState {
    return if (event.liability.amount > event.payer.balance) {
      gameState
        .update(event.payerId, event.payer.detractFunds(event.liability.amount))
        .update(event.liability.recevierId, event.liability.recevier.addFunds(event.payer.balance))
    } else {
      gameState
        .update(event.payerId, event.payer.detractFunds(event.liability.amount))
        .update(event.liability.recevierId, event.liability.recevier.addFunds(event.liability.amount))
    }
  }
}
