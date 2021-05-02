package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

class GivePlayerMoney(
  private val receiver: Player,
  private val receiversId: UUID,
  private val toGive: BigInteger,
  private val target: GameState,
) : Command {
  override fun execute(): GameState {
      return target.updatePlayer(receiversId, newBalance = receiver.balance + toGive)
  }
}
