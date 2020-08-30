package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import java.util.*

class PlayerMovementRequest(
  private val playersId: UUID,
  private val createMove: (Player, UUID, GameState) -> MovePlayer,
  private val context: GameState
) : Request {
  override fun validate(): Result<Command> {
    val player = context.players[playersId] ?: return INVALID_PLAYER_ID
    return Result.success(createMove(player, playersId, context))
  }
}
