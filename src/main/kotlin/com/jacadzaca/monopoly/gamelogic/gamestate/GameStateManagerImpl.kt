package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PropertyPurchaseEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.gamelogic.tiles.TileManager

internal class GameStateManagerImpl(private val tileManager: TileManager,
                                    private val buildingFactory: BuildingFactory,
                                    private val playerMover: PlayerMover) :
  GameStateManager {
  override fun applyEvent(event: GameEvent, gameState: GameState): GameState {
    return event.apply(this, gameState)
  }

  override fun applyEvent(event: MoveEvent, gameState: GameState): GameState {
    val movedPlayer = playerMover.move(gameState.getPlayer(event.playerId), gameState.boardSize)
    return gameState.update(event.playerId, movedPlayer)
  }

  override fun applyEvent(event: PropertyPurchaseEvent, gameState: GameState): GameState {
    val buyer = gameState.getPlayer(event.playerId)
    val tileWithEstate = tileManager.buyProperty(buyer, gameState.getTile(event.whereToBuy), event.propertyType)
    return gameState
      .update(event.whereToBuy, tileWithEstate)
      .update(event.playerId, buyer.detractFunds(buildingFactory.getPriceFor(event.propertyType)))
  }
}
