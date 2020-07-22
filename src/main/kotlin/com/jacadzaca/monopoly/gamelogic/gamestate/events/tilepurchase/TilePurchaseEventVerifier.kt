package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidTileIndex
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

internal class TilePurchaseEventVerifier(
  private val tileExists: (Int, GameState) -> Boolean
) : GameEventVerifier<TilePurchaseEvent> {
  companion object {
    internal const val tileAlreadyHasOwner = "Tile that the player wants to buy already has an owner "
  }

  override fun verify(event: TilePurchaseEvent, gameState: GameState): VerificationResult {
    val buyer = gameState.players[event.playerId] ?: return VerificationResult.Failure(invalidPlayerId)
    if (!tileExists(event.tileIndex, gameState)) {
      return VerificationResult.Failure(invalidTileIndex)
    }
    val tile = gameState.tiles[event.tileIndex]
    return when {
      tile.owner != null -> VerificationResult.Failure(tileAlreadyHasOwner)
      tile.price > buyer.balance -> VerificationResult.Failure(buyerHasInsufficientBalance)
      else -> VerificationResult.VerifiedTilePurchaseEvent(buyer, event.playerId, tile, event.tileIndex)
    }
  }
}
