package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

sealed class GameEvent {
  data class MoveEvent(val moverId: PlayerID)
  data class TilePurchaseEvent(val buyerId: PlayerID, val tileIndex: Int)
  data class PlayerPaysLiabilityEvent(val payer: Player, val payerId: PlayerID, val liability: Liability)
}
