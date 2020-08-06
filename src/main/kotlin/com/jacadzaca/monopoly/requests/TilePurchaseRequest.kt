package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.requests.Request.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.requests.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.transformations.TilePurchase
import java.util.*

class TilePurchaseRequest(
  private val buyersId: UUID,
  private val context: GameState
) : Request {
  internal companion object {
    internal const val tileAlreadyHasOwner = "Tile that the player wants to buy already has an owner "
  }

  override fun validate(): ValidationResult {
    val buyer = context.players[buyersId] ?: return ValidationResult.Failure(invalidPlayerId)
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != null -> ValidationResult.Failure(tileAlreadyHasOwner)
      tile.price > buyer.balance -> ValidationResult.Failure(buyerHasInsufficientBalance)
      else -> ValidationResult.Success(
        TilePurchase(
          buyer,
          buyersId,
          tile,
          buyer.position,
          context
        )
      )
    }
  }
}
