package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

internal class PlayerJoinValidator : RequestValidator<PlayerAction.JoinAction> {
  override fun validate(playersId: UUID, action: PlayerAction.JoinAction, context: GameState): Computation<Command> {
    return Computation.success(JoinPlayer(playersId, context))
  }
}
