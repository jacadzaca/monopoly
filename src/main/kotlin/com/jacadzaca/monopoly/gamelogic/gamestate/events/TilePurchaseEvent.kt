package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateManager
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class TilePurchaseEvent(
  override val playerId: PlayerID,
  val tileIndex: Int
) : GameEvent {
  override fun apply(gameStateManager: GameStateManager, gameState: GameState): GameState {
    return gameStateManager.applyTilePurchase(this, gameState)
  }
}
