package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Liability
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.*

class LiabilityPayment(
  private val payer: Player,
  private val payerId: UUID,
  private val liability: Liability,
  private val target: GameState
) : Transformation() {
  override fun transform(): GameState {
    return if (liability.amount > payer.balance) {
      target
        .update(payerId, payer.detractFunds(liability.amount))
        .update(liability.recevierId, liability.recevier.addFunds(payer.balance))
    } else {
      target
        .update(payerId, payer.detractFunds(liability.amount))
        .update(liability.recevierId, liability.recevier.addFunds(liability.amount))
    }
  }
}
