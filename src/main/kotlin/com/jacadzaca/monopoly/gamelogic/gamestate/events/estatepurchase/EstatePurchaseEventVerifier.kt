package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier

internal class EstatePurchaseEventVerifier(private val estateFactory: BuildingFactory, private val requiredHousesForHotel: Int) :
  GameEventVerifier<PropertyPurchaseEvent> {
  override fun verify(event: PropertyPurchaseEvent, gameState: GameState): PropertyPurchaseEvent? {
    val tile = gameState.tiles[event.tileIndex]
    val buyer = gameState.players.getValue(event.playerId)
    return when {
      tile.owner != buyer.id -> null
      estateFactory.getPriceFor(event.propertyType) > buyer.balance -> null
      event.propertyType == BuildingType.HOTEL && tile.houseCount() < requiredHousesForHotel -> null
      else -> event
    }
  }
}
