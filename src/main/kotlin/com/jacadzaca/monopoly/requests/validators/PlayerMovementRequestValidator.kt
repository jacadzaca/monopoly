package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.validators.*
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import java.util.*

class PlayerMovementRequestValidator(
  private val createMove: (Player, UUID, GameState) -> MovePlayer,
) : RequestValidator<PlayerAction.MoveAction> {
  override fun validate(playersId: UUID, action: PlayerAction.MoveAction, context: GameState): Computation<Command> {
    val player = context.players[playersId] ?: return INVALID_PLAYER_ID
    return if (context.isPlayersTurn(playersId)) {
      Computation.success(createMove(player, playersId, context))
    } else {
      NOT_PLAYERS_TURN
    }
  }
}
