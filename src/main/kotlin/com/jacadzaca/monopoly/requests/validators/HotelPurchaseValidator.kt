package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.*

internal class HotelPurchaseValidator(
  private val estate: Estate,
  private val requiredHousesForHotel: Int,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate,
) : RequestValidator {
  internal companion object {
    internal val NOT_ENOUGH_HOUSES =
      Computation.failure<Command>("There are not enough houses on the tile where a hotel is to be placed")
  }

  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      !context.isPlayersTurn(playersId) -> NOT_PLAYERS_TURN
      tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
      estate.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      tile.houseCount() < requiredHousesForHotel -> NOT_ENOUGH_HOUSES
      else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, estate, context))
    }
  }
}
