package com.jacadzaca.monopoly.gamelogic.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Transformation
import java.util.*

data class LiabilityPayment(
  private val payer: Player,
  private val payerId: UUID,
  private val liability: Liability,
  private val target: GameState
) : Transformation(target) {
  override fun apply(): GameState {
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
