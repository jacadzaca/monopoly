package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.*

class HousePurchaseValidator(
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate,
  private val house: Estate.House
) : RequestValidator {

  override fun validate(playersId: UUID, context: GameState): Computation<Command> {
    val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
      house.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, house, context))
    }
  }
}
