package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

class PayLiability(
  private val payer: Player,
  private val payerId: UUID,
  private val receiver: Player,
  private val receiversId: UUID,
  private val liability: BigInteger,
  private val target: GameState
) : Command {
  override fun execute(): GameState {
    return if (liability > payer.balance) {
      target
        .updatePlayer(payerId, newBalance = payer.balance - liability)
        .updatePlayer(receiversId, newBalance = receiver.balance + payer.balance)
    } else {
      target
        .updatePlayer(payerId, newBalance = payer.balance - liability)
        .updatePlayer(receiversId, newBalance = receiver.balance + liability)
    }
  }
}
