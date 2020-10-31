package com.jacadzaca.monopoly.requests

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
      Result.failure<Command>(Request.ValidationException("Buyer dose not own the tile where he wants to buy a estate"))
    internal val NOT_ENOUGH_HOUSES =
      Result.failure<Command>(Request.ValidationException("There are not enough houses on the tile where a hotel is to be placed"))
  }

  override fun validate(): Result<Command> {
    val buyer = context.players[buyersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != buyersId -> TILE_NOT_OWNED_BY_BUYER
      estate.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      estate is Estate.Hotel && tile.houseCount() < requiredHousesForHotel -> NOT_ENOUGH_HOUSES
      else -> Result.success(createPurchase(buyer, buyersId, tile, buyer.position, estate, context))
    }
  }
}
