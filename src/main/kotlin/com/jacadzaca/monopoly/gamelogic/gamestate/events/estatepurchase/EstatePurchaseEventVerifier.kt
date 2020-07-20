package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier

internal class EstatePurchaseEventVerifier(private val estateFactory: EstateFactory, private val requiredHousesForHotel: Int) :
  GameEventVerifier<EstatePurchaseEvent, VerifiedEstatePurchaseEvent> {
  override fun verify(event: EstatePurchaseEvent, gameState: GameState): VerifiedEstatePurchaseEvent? {
    val tile = gameState.tiles[event.tileIndex]
    val buyer = gameState.players.getValue(event.playerId)
    return when {
      tile.owner != buyer.id -> null
      estateFactory.getPriceFor(event.estateType) > buyer.balance -> null
      event.estateType == EstateType.HOTEL && tile.houseCount() < requiredHousesForHotel -> null
      else -> VerifiedEstatePurchaseEvent(event)
    }
  }
}
