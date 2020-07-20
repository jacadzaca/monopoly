package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

internal class TilePurchaseEventVerifier : GameEventVerifier<TilePurchaseEvent> {
  override fun verify(event: TilePurchaseEvent, gameState: GameState): TilePurchaseEvent? {
    val tile = gameState.tiles[event.tileIndex]
    val buyer = gameState.players.getValue(event.playerId)
    return when {
      tile.owner != null -> null
      tile.price > buyer.balance -> null
      else -> event
    }
  }
}
