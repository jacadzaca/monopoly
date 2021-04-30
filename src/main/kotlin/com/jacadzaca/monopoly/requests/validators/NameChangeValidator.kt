package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import java.util.*

internal class NameChangeValidator(
  private val createNameChange: (UUID, String, GameState) -> ChangeName
) : RequestValidator<PlayerAction.NameChangeAction> {
  internal companion object {
      internal val PLAYER_ALREADY_NAMED = Computation.failure<Command>("The player is already named")
  }
  override fun validate(playersId: UUID, action: PlayerAction.NameChangeAction, context: GameState): Computation<Command> {
    val player = context.players[playersId] ?: return INVALID_PLAYER_ID
    return when {
        player.name != null -> PLAYER_ALREADY_NAMED
        else -> Computation.success(createNameChange(playersId, action.newName, context))
    }
  }
}

