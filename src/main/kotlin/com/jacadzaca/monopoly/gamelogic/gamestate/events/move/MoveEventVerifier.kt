package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

class MoveEventVerifier : GameEventVerifier<MoveEvent> {
  override fun verify(event: MoveEvent, gameState: GameState): VerificationResult {
    val player = gameState.players[event.playerId] ?: return VerificationResult.Failure(invalidPlayerId)
    return VerificationResult.VerifiedMoveEvent(player, event.playerId)
  }
}
