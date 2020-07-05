package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import java.util.*

data class PropertyPurchaseEvent(
  override val playerId: PlayerID,
  val propertyType: BuildingType,
  val tileIndex: Int
) : GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState {
    return gameStateManager.applyEstatePurchase(this, gameState)
  }
}
