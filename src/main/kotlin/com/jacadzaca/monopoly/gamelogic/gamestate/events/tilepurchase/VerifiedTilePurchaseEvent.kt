package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerifiedGameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class VerifiedTilePurchaseEvent(private val tilePurchaseEvent: TilePurchaseEvent) : VerifiedGameEvent {
  val playerId: PlayerID
    get() = tilePurchaseEvent.playerId
  val tileIndex: Int
    get() = tilePurchaseEvent.tileIndex
}
