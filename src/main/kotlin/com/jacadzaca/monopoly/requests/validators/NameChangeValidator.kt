package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.commands.ChangeName
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import java.util.UUID

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
