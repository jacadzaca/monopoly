package com.jacadzaca.monopoly.gamelogic.move

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.VerificationResult.VerifiedMoveEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover

internal class MoveEventApplier(private val playerMover: PlayerMover) :
  GameEventApplier<VerifiedMoveEvent> {
  override fun apply(event: VerifiedMoveEvent, gameState: GameState): GameState {
    return gameState
      .update(event.moverId, playerMover.move(event.mover, gameState.boardSize))
  }
}
