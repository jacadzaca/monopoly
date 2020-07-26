package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateChange
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import java.util.*

data class PlayerPaysLiabilityEvent(
  val payerId: UUID,
  val payer: Player,
  val liability: Liability
) : GameStateChange
