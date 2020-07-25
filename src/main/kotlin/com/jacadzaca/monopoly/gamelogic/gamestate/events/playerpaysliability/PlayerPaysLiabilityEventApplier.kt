package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier

internal class PlayerPaysLiabilityEventApplier : GameEventApplier<GameEvent.PlayerPaysLiabilityEvent> {
  override fun apply(event: GameEvent.PlayerPaysLiabilityEvent, gameState: GameState): GameState {
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
