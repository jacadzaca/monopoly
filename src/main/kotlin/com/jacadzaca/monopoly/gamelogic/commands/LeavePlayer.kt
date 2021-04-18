package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

data class LeavePlayer(
  private val playersId: UUID,
  private val gameState: GameState,
) : Command() {
  override fun asEvent(): Event = Event.PlayerLeft(playersId)

  override fun execute(): GameState {
    val changedGameState = gameState.remove(playersId).disownPlayer(playersId)
    return if (gameState.isPlayersTurn(playersId)) {
      ChangeTurn(changedGameState).apply()
    } else {
      changedGameState
    }
  }
}
