package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.GameEventVerifier.Companion.invalidTileIndex
import com.jacadzaca.monopoly.gamelogic.VerificationResult

internal class TilePurchaseEventVerifier(
  private val tileExists: (Int, GameState) -> Boolean
) : GameEventVerifier<TilePurchaseEvent> {
  companion object {
    internal const val tileAlreadyHasOwner = "Tile that the player wants to buy already has an owner "
  }

  override fun verify(event: TilePurchaseEvent, gameState: GameState): VerificationResult {
    val buyer = gameState.players[event.buyerId] ?: return VerificationResult.Failure(invalidPlayerId)
    if (!tileExists(event.tileIndex, gameState)) {
      return VerificationResult.Failure(invalidTileIndex)
    }
    val tile = gameState.tiles[event.tileIndex]
    return when {
      tile.owner != null -> VerificationResult.Failure(tileAlreadyHasOwner)
      tile.price > buyer.balance -> VerificationResult.Failure(buyerHasInsufficientBalance)
      else -> VerificationResult.VerifiedTilePurchaseEvent(buyer, event.buyerId, tile, event.tileIndex)
    }
  }
}
