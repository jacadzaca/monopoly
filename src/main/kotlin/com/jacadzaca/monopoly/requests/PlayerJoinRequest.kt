package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.serialization.Serializable
import java.util.*

class PlayerJoinRequest(val playersId: UUID) : Request {
  override fun validate(context: GameState): Computation<Command> {
    return Computation.success(JoinPlayer(playersId, context))
  }

  override fun playersId(): UUID = playersId
}
