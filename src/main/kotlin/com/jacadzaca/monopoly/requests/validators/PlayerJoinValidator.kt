package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.gamelogic.commands.JoinPlayer
import com.jacadzaca.monopoly.requests.PlayerAction
import java.util.UUID

internal class PlayerJoinValidator : RequestValidator<PlayerAction.JoinAction> {
    internal companion object {
        internal val newPlayer = Player(balance = System.getProperty("starting_balance").toBigInteger())
    }
    override fun validate(playersId: UUID, action: PlayerAction.JoinAction, context: GameState): Computation<Command> {
        return Computation.success(JoinPlayer(newPlayer, playersId, context))
    }
}
