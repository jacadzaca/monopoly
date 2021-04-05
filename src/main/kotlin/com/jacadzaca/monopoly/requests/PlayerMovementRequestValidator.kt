package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.NOT_PLAYERS_TURN
import java.util.*

class PlayerMovementRequestValidator(
  private val createMove: (Player, UUID, GameState) -> MovePlayer,
) : RequestValidator {
  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    val player = context.players[playersId] ?: return INVALID_PLAYER_ID
    return if (context.isPlayersTurn(playersId)) {
      Computation.success(createMove(player, playersId, context))
    } else {
      NOT_PLAYERS_TURN
    }
  }
}
