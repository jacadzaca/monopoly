package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.*

internal class HousePurchaseValidator(
  private val house: Estate.House,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate
) : RequestValidator<PlayerAction.BuyHouseAction> {
  override fun validate(playersId: UUID, action: PlayerAction.BuyHouseAction, context: GameState): Computation<Command> {
    val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
    val tile = context.tiles[buyer.position]
    return when {
      !context.isPlayersTurn(playersId) -> NOT_PLAYERS_TURN
      tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
      house.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
      else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, house, context))
    }
  }
}
