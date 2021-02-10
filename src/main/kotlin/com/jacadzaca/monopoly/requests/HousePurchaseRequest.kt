package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.Request.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.*

class HousePurchaseRequest(
  private val buyersId: UUID,
  private val house: Estate.House,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate
) : Request {
  override fun validate(context: GameState): Computation<Command> {
    val buyer = context.players[buyersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != buyersId -> TILE_NOT_OWNED_BY_BUYER
      house.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      else -> Computation.success(createPurchase(buyer, buyersId, tile, buyer.position, house, context))
    }
  }

  override fun playersId(): UUID = buyersId
}
