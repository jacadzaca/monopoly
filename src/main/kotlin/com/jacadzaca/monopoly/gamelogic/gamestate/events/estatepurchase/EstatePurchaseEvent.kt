package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class EstatePurchaseEvent(
  val buyer: PlayerID,
  val estateType: EstateType,
  val tileIndex: Int
)
