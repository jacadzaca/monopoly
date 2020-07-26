package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import java.util.*

data class EstatePurchaseEvent(
  val buyerId: UUID,
  val estateType: EstateType,
  val tileIndex: Int
)
