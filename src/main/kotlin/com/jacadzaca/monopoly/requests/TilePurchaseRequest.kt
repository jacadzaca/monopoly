package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import java.util.*

class TilePurchaseRequest(
  private val buyersId: UUID,
  private val context: GameState,
  private val createPurchase: (Player, UUID, Tile, Int, GameState) -> (BuyTile)
) : Request {
  internal companion object {
    internal val TILE_ALREADY_HAS_OWNER = ComputationResult.failure<Command>("Tile that the player wants to buy already has an owner")
  }

  override fun validate(): ComputationResult<Command> {
    val buyer = context.players[buyersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != null -> TILE_ALREADY_HAS_OWNER
      tile.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      else -> ComputationResult.success(createPurchase(buyer, buyersId, tile, buyer.position, context))
    }
  }
}
