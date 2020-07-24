package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier

class PlayerPaysLiabilityEventApplier : GameEventApplier<PlayerPaysLiabilityEvent> {
  override fun apply(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState {
    return if (event.liability.howMuch > event.payer.balance) {
      gameState
        .update(event.playerId, event.payer.detractFunds(event.liability.howMuch))
        .update(event.liability.recevierId, event.liability.recevier.addFunds(event.payer.balance))
    } else {
      gameState
        .update(event.playerId, event.payer.detractFunds(event.liability.howMuch))
        .update(event.liability.recevierId, event.liability.recevier.addFunds(event.liability.howMuch))
    }
  }
}
