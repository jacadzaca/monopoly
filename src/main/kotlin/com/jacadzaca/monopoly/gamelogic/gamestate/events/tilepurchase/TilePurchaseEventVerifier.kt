package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier

internal class TilePurchaseEventVerifier :
  GameEventVerifier<TilePurchaseEvent, VerifiedTilePurchaseEvent> {
  override fun verify(event: TilePurchaseEvent, gameState: GameState): VerifiedTilePurchaseEvent? {
    val tile = gameState.tiles[event.tileIndex]
    val buyer = gameState.players.getValue(event.playerId)
    return when {
      tile.owner != null -> null
      tile.price > buyer.balance -> null
      else -> VerifiedTilePurchaseEvent(event)
    }
  }
}
