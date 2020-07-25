package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

internal class MoveEventVerifier : GameEventVerifier<GameEvent.MoveEvent> {
  override fun verify(event: GameEvent.MoveEvent, gameState: GameState): VerificationResult {
    val player = gameState.players[event.moverId] ?: return VerificationResult.Failure(invalidPlayerId)
    return VerificationResult.VerifiedMoveEvent(player, event.moverId)
  }
}
