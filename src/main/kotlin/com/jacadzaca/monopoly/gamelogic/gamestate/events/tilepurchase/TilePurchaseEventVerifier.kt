package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

internal class TilePurchaseEventVerifier(
  private val tileExists: (Int, GameState) -> Boolean
) : GameEventVerifier<TilePurchaseEvent, VerifiedTilePurchaseEvent> {
  override fun verify(event: TilePurchaseEvent, gameState: GameState): VerifiedTilePurchaseEvent? {
    val buyer = gameState.players[event.playerId]
    if (!tileExists(event.tileIndex, gameState) || buyer == null) {
      return null
    }
    val tile = gameState.tiles[event.tileIndex]
    return when {
      tile.owner != null -> null
      tile.price > buyer.balance -> null
      else -> VerifiedTilePurchaseEvent(event)
    }
  }
}
