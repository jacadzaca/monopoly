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
  override fun asEvent(): Event = Event.LiabilityPaid(payerId, receiversId, liability)

  override fun execute(): GameState {
    return if (liability > payer.balance) {
      target
        .put(payerId, payer.detractFunds(liability))
        .put(receiversId, receiver.addFunds(payer.balance))
    } else {
      target
        .put(payerId, payer.detractFunds(liability))
        .put(receiversId, receiver.addFunds(liability))
    }
  }
}
