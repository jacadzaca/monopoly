package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

data class PlayerMovementRequest(private val playersId: UUID) : Request {
  override fun validate(context: GameState): ValidationResult {
    val player = context.players[playersId] ?: return ValidationResult.Failure(invalidPlayerId)
    return ValidationResult.Success(PlayerMoves.create(player, playersId))
  }
}
