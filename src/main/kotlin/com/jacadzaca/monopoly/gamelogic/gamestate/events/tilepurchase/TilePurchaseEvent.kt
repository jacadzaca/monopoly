package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class TilePurchaseEvent(
  val buyerId: PlayerID,
  val tileIndex: Int
)
