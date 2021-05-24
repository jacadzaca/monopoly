package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.gamelogic.commands.MovePlayer
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import java.util.UUID

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
