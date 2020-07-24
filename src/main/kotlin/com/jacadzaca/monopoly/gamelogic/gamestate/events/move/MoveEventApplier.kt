package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult.VerifiedMoveEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover

class MoveEventApplier(private val playerMover: PlayerMover) : GameEventApplier<VerifiedMoveEvent> {
  override fun apply(event: VerifiedMoveEvent, gameState: GameState): GameState {
    return gameState
      .update(event.moverId, playerMover.move(event.mover, gameState.boardSize))
  }
}
