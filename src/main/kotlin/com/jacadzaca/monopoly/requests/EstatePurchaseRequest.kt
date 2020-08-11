package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.transformations.EstatePurchase
import com.jacadzaca.monopoly.requests.Request.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.requests.Request.Companion.invalidPlayerId
import java.util.*

class EstatePurchaseRequest(
  private val buyersId: UUID,
  private val estate: Estate,
  private val requiredHousesForHotel: Int,
  private val createPurchase: (Player, UUID, Tile, Int, Estate, GameState) -> EstatePurchase,
  private val context: GameState
) : Request {
  internal companion object {
    internal const val tileNotOwnedByBuyer = "Buyer dose not own the tile where he wants to buy a estate"
    internal const val notEnoughHouses = "There are not enough houses on the tile where a hotel is to be placed "
  }
  override fun validate(): ValidationResult {
    val buyer = context.players[buyersId] ?: return ValidationResult.Failure(invalidPlayerId)
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != buyersId -> ValidationResult.Failure(tileNotOwnedByBuyer)
      estate.price > buyer.balance -> ValidationResult.Failure(buyerHasInsufficientBalance)
      estate is Estate.Hotel && tile.houseCount() < requiredHousesForHotel -> ValidationResult.Failure(notEnoughHouses)
      else -> ValidationResult.Success(createPurchase(buyer, buyersId, tile, buyer.position, estate, context))
    }
  }
}
