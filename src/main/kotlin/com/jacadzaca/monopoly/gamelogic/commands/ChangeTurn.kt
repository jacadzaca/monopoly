package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*

class ChangeTurn(private val gameState: GameState) : Command() {
  override fun asEvent(): Event = Event.TurnChanged

  override fun execute(): GameState {
    return if (gameState.turnOrder.size == 0) {
      gameState
    } else {
      gameState.updateTurn((gameState.currentTurn + 1) % gameState.turnOrder.size)
    }
  }
}
