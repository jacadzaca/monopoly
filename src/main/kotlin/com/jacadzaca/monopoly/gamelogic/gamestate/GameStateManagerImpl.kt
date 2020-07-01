package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.events.*
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.gamelogic.tiles.TileManager

internal class GameStateManagerImpl(private val tileManager: TileManager,
                                    private val buildingFactory: BuildingFactory,
                                    private val playerMover: PlayerMover) :
  GameStateManager {
  override fun applyEvent(event: MoveEvent, gameState: GameState): GameState {
    return gameState.update(event.playerId, playerMover.move(gameState.getPlayer(event.playerId), gameState.boardSize))
  }

  override fun applyEvent(event: TilePurchaseEvent, gameState: GameState): GameState {
    val buyer = gameState.getPlayer(event.playerId)
    val tileToBuy = gameState.getTile(event.tileIndex)
    return gameState
      .update(event.tileIndex, tileManager.buyTile(buyer, tileToBuy))
      .update(event.playerId, buyer.detractFunds(tileToBuy.price))
  }

  override fun applyEvent(event: PropertyPurchaseEvent, gameState: GameState): GameState {
    val buyer = gameState.getPlayer(event.playerId)
    val tileWithEstate = tileManager.buyProperty(buyer, gameState.getTile(event.tileIndex), event.propertyType)
    return gameState
      .update(event.tileIndex, tileWithEstate)
      .update(event.playerId, buyer.detractFunds(buildingFactory.getPriceFor(event.propertyType)))
  }

  override fun applyEvent(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState {
    val payer = gameState.getPlayer(event.playerId)
    val receiver = gameState.getPlayer(event.liability.toWhom)
    return if (payer.balance < event.liability.howMuch) {
      gameState
        .update(event.playerId, payer.detractFunds(event.liability.howMuch))
        .update(event.liability.toWhom, receiver.addFunds(payer.balance))
    } else {
      gameState
        .update(event.playerId, payer.detractFunds(event.liability.howMuch))
        .update(event.liability.toWhom, receiver.addFunds(event.liability.howMuch))
    }
  }
}
