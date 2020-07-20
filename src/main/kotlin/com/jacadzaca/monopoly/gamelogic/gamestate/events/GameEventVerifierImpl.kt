package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

class GameEventVerifierImpl(private val estateFactory: BuildingFactory, private val requiredHousesForHotel: Int) : GameEventVerifier {
  override fun verifyTilePurchaseEvent(event: TilePurchaseEvent, gameState: GameState): TilePurchaseEvent? {
    val tile = gameState.tiles[event.tileIndex]
    val buyer = gameState.players.getValue(event.playerId)
    return when {
      tile.owner != null -> null
      tile.price > buyer.balance -> null
      else -> event
    }
  }

  override fun verifyEstatePurchaseEvent(
    event: PropertyPurchaseEvent,
    gameState: GameState
  ): PropertyPurchaseEvent? {
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
