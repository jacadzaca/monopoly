package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import java.util.*

class EstatePurchaseRequest(
  private val buyersId: UUID,
  private val estate: Estate,
  private val requiredHousesForHotel: Int,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate,
  private val context: GameState
) : Request {
  internal companion object {
    internal val TILE_NOT_OWNED_BY_BUYER =
      ComputationResult.failure<Command>("Buyer dose not own the tile where he wants to buy a estate")
    internal val NOT_ENOUGH_HOUSES =
      ComputationResult.failure<Command>("There are not enough houses on the tile where a hotel is to be placed")
  }

  override fun validate(): ComputationResult<Command> {
    val buyer = context.players[buyersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != buyersId -> TILE_NOT_OWNED_BY_BUYER
      estate.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      estate is Estate.Hotel && tile.houseCount() < requiredHousesForHotel -> NOT_ENOUGH_HOUSES
      else -> ComputationResult.success(createPurchase(buyer, buyersId, tile, buyer.position, estate, context))
    }
  }
}
