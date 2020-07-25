package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

sealed class GameEvent {
  data class MoveEvent(val moverId: PlayerID): GameEvent()
  data class TilePurchaseEvent(val buyerId: PlayerID, val tileIndex: Int): GameEvent()
  data class EstatePurchaseEvent(val buyerId: PlayerID, val estateType: EstateType, val tileIndex: Int): GameEvent()
}
