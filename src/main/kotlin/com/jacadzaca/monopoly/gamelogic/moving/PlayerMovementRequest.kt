package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

data class PlayerMovementRequest(
  private val playersId: UUID,
  private val actionCreator: (Player, UUID, GameState) -> PlayerMoves,
  private val context: GameState
) : Request {
  override fun validate(): ValidationResult {
    val player = context.players[playersId] ?: return ValidationResult.Failure(invalidPlayerId)
    return ValidationResult.Success(actionCreator(player, playersId, context))
  }
}
