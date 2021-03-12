package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.Request.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.Request.Companion.TILE_NOT_OWNED_BY_BUYER
import kotlinx.serialization.Transient
import java.util.*

class HotelPurchaseRequest(
  private val buyersId: UUID,
  private val estate: Estate,
  private val requiredHousesForHotel: Int,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate,
) : Request {
  internal companion object {
    internal val NOT_ENOUGH_HOUSES =
      Computation.failure<Command>("There are not enough houses on the tile where a hotel is to be placed")
  }

  override fun validate(context: GameState): Computation<Command> {
    val buyer = context.players[buyersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      !context.isPlayersTurn(buyersId) -> NOT_PLAYERS_TURN
      tile.ownersId != buyersId -> TILE_NOT_OWNED_BY_BUYER
      estate.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      tile.houseCount() < requiredHousesForHotel -> NOT_ENOUGH_HOUSES
      else -> Computation.success(createPurchase(buyer, buyersId, tile, buyer.position, estate, context))
    }
  }

  override fun playersId(): UUID = buyersId
}
