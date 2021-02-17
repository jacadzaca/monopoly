package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*

class ChangeTurn(private val target: GameState) : Command {
  override fun asEvent(): Event = Event.TurnChanged

  override fun execute(): GameState {
    return target
      .copy(currentTurn = (target.currentTurn + 1) % target.turnOrder.size)
  }
}
