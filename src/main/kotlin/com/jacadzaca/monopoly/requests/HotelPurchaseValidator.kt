package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.*

class HotelPurchaseValidator(
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate,
  private val requiredHosesForHotel: Int,
  private val hotel: Estate.Hotel
) : RequestValidator {
  internal companion object {
    internal val NOT_ENOUGH_HOUSES =
      Computation.failure<Command>("There are not enough houses on the tile where a hotel is to be placed")
  }
  init {
    if (requiredHosesForHotel < 0) throw IllegalStateException("Cannot require a negative number of hotels")
  }

  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
      hotel.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      tile.houseCount() < requiredHosesForHotel -> NOT_ENOUGH_HOUSES
      else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, hotel, context))
    }

  }
}
