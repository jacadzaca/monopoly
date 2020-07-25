package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateChange
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class PlayerPaysLiabilityEvent(
  val payerId: PlayerID,
  val payer: Player,
  val liability: Liability
) : GameStateChange
