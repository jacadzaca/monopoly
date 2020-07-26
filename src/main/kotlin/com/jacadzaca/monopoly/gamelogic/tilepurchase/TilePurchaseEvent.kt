package com.jacadzaca.monopoly.gamelogic.tilepurchase

import java.util.*

data class TilePurchaseEvent(
  val buyerId: UUID,
  val tileIndex: Int
)
