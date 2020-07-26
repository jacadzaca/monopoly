package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.VerificationResult

internal class MoveEventVerifier : GameEventVerifier<MoveEvent> {
  override fun verify(event: MoveEvent, gameState: GameState): VerificationResult {
    val player = gameState.players[event.moverId] ?: return VerificationResult.Failure(invalidPlayerId)
    return VerificationResult.VerifiedMoveEvent(player, event.moverId)
  }
}
