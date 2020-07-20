package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerifiedGameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class VerifiedEstatePurchaseEvent(private val estatePurchaseEvent: EstatePurchaseEvent) : VerifiedGameEvent {
  val playerId: PlayerID
    get() = estatePurchaseEvent.playerId
  val estateType: EstateType
    get() = estatePurchaseEvent.estateType
  val tileIndex: Int
    get() = estatePurchaseEvent.tileIndex
}
