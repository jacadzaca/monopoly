package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

internal class EstatePurchaseEventVerifier(
  private val estateFactory: EstateFactory,
  private val requiredHousesForHotel: Int,
  private val tileExists: (Int, GameState) -> Boolean
) : GameEventVerifier<EstatePurchaseEvent, VerifiedEstatePurchaseEvent> {
  override fun verify(event: EstatePurchaseEvent, gameState: GameState): VerifiedEstatePurchaseEvent? {
    val buyer = gameState.players[event.playerId]
    if (!tileExists(event.tileIndex, gameState) || buyer == null) {
      return null
    }
    val tile = gameState.tiles[event.tileIndex]
    return when {
      tile.owner != buyer.id -> null
      estateFactory.getPriceFor(event.estateType) > buyer.balance -> null
      event.estateType == EstateType.HOTEL && tile.houseCount() < requiredHousesForHotel -> null
      else -> VerifiedEstatePurchaseEvent(event)
    }
  }
}
