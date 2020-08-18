package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.MovePlayer
import com.jacadzaca.monopoly.requests.Request.Companion.invalidPlayerId
import java.util.*

class PlayerMovementRequest(
  private val playersId: UUID,
  private val createMove: (Player, UUID, GameState) -> MovePlayer,
  private val context: GameState
) : Request {
  override fun validate(): ValidationResult {
    val player = context.players[playersId] ?: return ValidationResult.Failure(invalidPlayerId)
    return ValidationResult.Success(createMove(player, playersId, context))
  }
}
