package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult

internal class EstatePurchaseEventVerifier(
  private val estateFactory: EstateFactory,
  private val requiredHousesForHotel: Int,
  private val tileExists: (Int, GameState) -> Boolean
) : GameEventVerifier<EstatePurchaseEvent> {
  companion object {
    internal const val tileNotOwnedByBuyer = "Buyer dose not own the tile where he wants to buy a estate"
    internal const val notEnoughHouses = "There are not enough houses on the tile where a hotel is to be placed "
  }

  override fun verify(event: EstatePurchaseEvent, gameState: GameState): VerificationResult {
    val buyer =
      gameState.players[event.buyerId] ?: return VerificationResult.Failure(GameEventVerifier.invalidPlayerId)
    if (!tileExists(event.tileIndex, gameState)) {
      return VerificationResult.Failure(GameEventVerifier.invalidTileIndex)
    }
    val tile = gameState.tiles[event.tileIndex]
    return when {
      tile.owner != event.buyerId -> VerificationResult.Failure(tileNotOwnedByBuyer)
      estateFactory.getPriceFor(event.estateType) > buyer.balance -> VerificationResult.Failure(GameEventVerifier.buyerHasInsufficientBalance)
      event.estateType == EstateType.HOTEL && tile.houseCount() < requiredHousesForHotel -> VerificationResult.Failure(
        notEnoughHouses
      )
      else -> VerificationResult.VerifiedEstatePurchaseEvent(
        buyer,
        event.buyerId,
        tile,
        event.tileIndex,
        event.estateType
      )
    }
  }
}
