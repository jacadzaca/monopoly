package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

class PlayerLeaveRequest(val playersId: UUID) : Request {
  override fun validate(context: GameState): Computation<Command> {
    return Computation.success(LeavePlayer(playersId, context))
  }

  override fun playersId(): UUID = playersId
}
