package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import java.util.*

data class PropertyPurchaseEvent(override val playerId: UUID,
                                 val propertyType: BuildingType,
                                 val whereToBuy: Int):
  GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState =
    gameStateManager.applyEvent(this, gameState)
}
