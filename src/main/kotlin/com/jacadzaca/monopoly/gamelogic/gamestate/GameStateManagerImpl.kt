package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.events.*
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.PropertyPurchaseEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.gamelogic.tiles.TileManager

internal class GameStateManagerImpl(private val tileManager: TileManager,
                                    private val buildingFactory: BuildingFactory,
                                    private val playerMover: PlayerMover) :
  GameStateManager {
  override fun applyPlayerMove(event: MoveEvent, gameState: GameState): GameState {
    return gameState.update(event.playerId, playerMover.move(gameState.players.getValue(event.playerId), gameState.boardSize))
  }

  override fun applyTilePurchase(event: TilePurchaseEvent, gameState: GameState): GameState {
    val buyer = gameState.players.getValue(event.playerId)
    val tileToBuy = gameState.tiles[event.tileIndex]
    return gameState
      .update(event.tileIndex, tileManager.buyTile(buyer, tileToBuy))
      .update(event.playerId, buyer.detractFunds(tileToBuy.price))
  }

  override fun applyEstatePurchase(event: PropertyPurchaseEvent, gameState: GameState): GameState {
    val buyer = gameState.players.getValue(event.playerId)
    val tileWithEstate = tileManager.buyProperty(buyer, gameState.tiles[event.tileIndex], event.propertyType)
    return gameState
      .update(event.tileIndex, tileWithEstate)
      .update(event.playerId, buyer.detractFunds(buildingFactory.getPriceFor(event.propertyType)))
  }

  override fun applyLiabilityPayment(event: PlayerPaysLiabilityEvent, gameState: GameState): GameState {
    val payer = gameState.players.getValue(event.playerId)
    val receiver = gameState.players.getValue(event.liability.toWhom)
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
