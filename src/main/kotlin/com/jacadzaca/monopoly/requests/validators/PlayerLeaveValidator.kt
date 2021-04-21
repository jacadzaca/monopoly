package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

internal class PlayerLeaveValidator : RequestValidator {
  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    return Computation.success(LeavePlayer(playersId, "player left", context))
  }
}
