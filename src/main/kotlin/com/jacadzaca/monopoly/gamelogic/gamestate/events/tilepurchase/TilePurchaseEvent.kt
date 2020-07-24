package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class TilePurchaseEvent(
  override val playerId: PlayerID,
  val tileIndex: Int
) : GameEvent
