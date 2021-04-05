package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

class PlayerLeaveRequest : Request {
  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    return Computation.success(LeavePlayer(playersId, context))
  }

  override fun playersId(): UUID = UUID.randomUUID()
}
