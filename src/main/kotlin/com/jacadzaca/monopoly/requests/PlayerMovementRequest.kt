package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.Request.Companion.NOT_PLAYERS_TURN
import java.util.*

class PlayerMovementRequest(
  private val playersId: UUID,
  private val createMove: (Player, UUID, GameState) -> MovePlayer,
) : Request {
  override fun validate(context: GameState): Computation<Command> {
    val player = context.players[playersId] ?: return INVALID_PLAYER_ID
    return if (context.isPlayersTurn(playersId)) {
      Computation.success(createMove(player, playersId, context))
    } else {
      NOT_PLAYERS_TURN
    }
  }

  override fun playersId(): UUID = playersId
}
