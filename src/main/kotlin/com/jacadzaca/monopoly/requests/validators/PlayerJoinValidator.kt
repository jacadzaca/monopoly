package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*
import java.math.BigInteger

internal class PlayerJoinValidator : RequestValidator<PlayerAction.JoinAction> {
  internal companion object {
      internal val newPlayer = Player(balance = System.getProperty("starting_balance").toBigInteger())
  }
  override fun validate(playersId: UUID, action: PlayerAction.JoinAction, context: GameState): Computation<Command> {
    return Computation.success(JoinPlayer(newPlayer, playersId, context))
  }
}
