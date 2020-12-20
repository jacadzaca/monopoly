package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import java.util.*

class TilePurchaseValidator(
  private val createPurchase: (Player, UUID, Tile, Int, GameState) -> (BuyTile)
) : RequestValidator {
  internal companion object {
    internal val TILE_ALREADY_HAS_OWNER =
      Computation.failure<Command>("Tile that the player wants to buy already has an owner")
  }

  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != null -> TILE_ALREADY_HAS_OWNER
      tile.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, context))
    }
  }
}
