package com.jacadzaca.monopoly.gamelogic.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.Transformation
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.*

data class PlayerPaysLiabilityEvent(
  val payerId: UUID,
  val payer: Player,
  val liability: Liability
) : Transformation
