package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import java.util.*

data class TilePurchaseEvent(
  val buyerId: UUID,
  val tileIndex: Int
)
