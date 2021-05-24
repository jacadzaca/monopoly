package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.gamelogic.commands.LeavePlayer
import com.jacadzaca.monopoly.requests.PlayerAction
import java.util.UUID

internal class PlayerLeaveValidator : RequestValidator<PlayerAction.LeaveAction> {
    override fun validate(playersId: UUID, action: PlayerAction.LeaveAction, context: GameState): Computation<Command> {
        return Computation.success(LeavePlayer(playersId, "player left", context))
    }
}
