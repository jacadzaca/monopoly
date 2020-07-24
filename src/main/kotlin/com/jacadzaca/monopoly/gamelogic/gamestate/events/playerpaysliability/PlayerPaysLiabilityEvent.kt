package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class PlayerPaysLiabilityEvent(
  override val playerId: PlayerID,
  val payer: Player,
  val liability: Liability
) : GameEvent
