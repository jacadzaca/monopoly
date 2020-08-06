package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Request
import com.jacadzaca.monopoly.gamelogic.Request.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.gamelogic.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.ValidationResult
import java.util.*

class TilePurchaseRequest(
  private val buyersId: UUID
) : Request {
  internal companion object {
    internal const val tileAlreadyHasOwner = "Tile that the player wants to buy already has an owner "
  }

  override fun validate(context: GameState): ValidationResult {
    val buyer = context.players[buyersId] ?: return ValidationResult.Failure(invalidPlayerId)
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != null -> ValidationResult.Failure(tileAlreadyHasOwner)
      tile.price > buyer.balance -> ValidationResult.Failure(buyerHasInsufficientBalance)
      else -> ValidationResult.Success(TilePurchase(buyer, buyersId, tile, buyer.position, context))
    }
  }
}
