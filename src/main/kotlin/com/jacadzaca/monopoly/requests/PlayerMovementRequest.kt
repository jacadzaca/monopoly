package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.requests.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.transformations.PlayerMoves
import java.util.*

class PlayerMovementRequest(
  private val playersId: UUID,
  private val actionCreator: (Player, UUID, GameState) -> PlayerMoves,
  private val context: GameState
) : Request {
  override fun validate(): ValidationResult {
    val player = context.players[playersId] ?: return ValidationResult.Failure(invalidPlayerId)
    return ValidationResult.Success(actionCreator(player, playersId, context))
  }
}
