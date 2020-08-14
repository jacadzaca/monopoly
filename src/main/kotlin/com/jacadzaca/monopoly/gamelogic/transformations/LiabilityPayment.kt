package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

class LiabilityPayment(
  private val payer: Player,
  private val payerId: UUID,
  private val receiver: Player,
  private val receiversId: UUID,
  private val liability: BigInteger,
  private val target: GameState
) : Transformation() {
  override fun transform(): GameState {
    return if (liability > payer.balance) {
      target
        .update(payerId, payer.detractFunds(liability))
        .update(receiversId, receiver.addFunds(payer.balance))
    } else {
      target
        .update(payerId, payer.detractFunds(liability))
        .update(receiversId, receiver.addFunds(liability))
    }
  }
}
