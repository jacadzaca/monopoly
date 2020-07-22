package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover

class MoveEventApplier(private val playerMover: PlayerMover) : GameEventApplier<VerificationResult.VerifiedMoveEvent> {
  override fun apply(event: VerificationResult.VerifiedMoveEvent, gameState: GameState): GameState {
    return gameState
      .update(event.playerId, playerMover.move(event.player, gameState.boardSize))
  }
}