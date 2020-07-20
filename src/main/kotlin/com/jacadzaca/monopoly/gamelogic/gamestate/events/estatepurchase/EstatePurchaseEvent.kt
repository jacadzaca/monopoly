package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class EstatePurchaseEvent(
  override val playerId: PlayerID,
  val estateType: EstateType,
  val tileIndex: Int
) : GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState {
    return gameStateManager.applyEstatePurchase(this, gameState)
  }
}
