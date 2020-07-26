package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.VerificationResult.VerifiedMoveEvent

internal class MoveEventApplier(private val rollDice: () -> Int) :
  GameEventApplier<VerifiedMoveEvent> {
  override fun apply(event: VerifiedMoveEvent, gameState: GameState): GameState {
    return gameState
      .update(event.moverId, event.mover.copy(position = (event.mover.position + rollDice()) % gameState.boardSize))
  }
}
