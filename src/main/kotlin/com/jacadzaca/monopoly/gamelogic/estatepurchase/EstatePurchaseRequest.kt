package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.Request.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import java.math.BigInteger
import java.util.*

data class EstatePurchaseRequest(
  private val buyersId: UUID,
  private val estateType: EstateType,
  private val priceOf: (EstateType) -> BigInteger,
  private val requiredHousesForHotel: Int,
  private val createAction: (Player, UUID, Tile, Int, EstateType) -> EstatePurchase
) : Request {
  internal companion object {
    internal const val tileNotOwnedByBuyer = "Buyer dose not own the tile where he wants to buy a estate"
    internal const val notEnoughHouses = "There are not enough houses on the tile where a hotel is to be placed "
  }
  override fun validate(context: GameState): ValidationResult {
    val buyer =
      context.players[buyersId] ?: return ValidationResult.Failure(invalidPlayerId)
    val tile = context.tiles[buyer.position]
    return when {
      tile.ownersId != buyersId -> ValidationResult.Failure(tileNotOwnedByBuyer)
      priceOf(estateType) > buyer.balance -> ValidationResult.Failure(buyerHasInsufficientBalance)
      estateType == EstateType.HOTEL && tile.houseCount() < requiredHousesForHotel -> ValidationResult.Failure(notEnoughHouses)
      else -> ValidationResult.Success(createAction(buyer, buyersId, tile, buyer.position, estateType))
    }
  }
}
