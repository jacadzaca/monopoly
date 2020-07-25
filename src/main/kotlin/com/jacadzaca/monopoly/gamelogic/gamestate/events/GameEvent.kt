package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.PlayerID

sealed class GameEvent {
  data class TilePurchaseEvent(val buyerId: PlayerID, val tileIndex: Int)
}
