package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

internal class PlayerLeaveValidator : RequestValidator<PlayerAction.LeaveAction> {
  override fun validate(playersId: UUID, action: PlayerAction.LeaveAction, context: GameState): Computation<Command> {
    return Computation.success(LeavePlayer(playersId, "player left", context))
  }
}
