package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*

class ChangeTurn(private val target: GameState) : Command() {
  override fun asEvent(): Event = Event.TurnChanged

  override fun execute(): GameState {
    return if (target.turnOrder.size == 0) {
      target.copy(currentTurn = 0)
    } else {
      target
        .copy(currentTurn = (target.currentTurn + 1) % target.turnOrder.size)
    }
  }
}
