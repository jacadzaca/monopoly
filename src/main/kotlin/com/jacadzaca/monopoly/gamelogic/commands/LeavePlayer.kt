package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

data class LeavePlayer(
  private val playersId: UUID,
  private val target: GameState,
  private val changeTurn: ChangeTurn = ChangeTurn(target),
) : Command {
  override fun asEvent(): Event = Event.PlayerLeft(playersId)

  override fun execute(): GameState {
    val changedTarget = target.remove(playersId).disownPlayer(playersId)
    return if (target.isPlayersTurn(playersId)) {
      ChangeTurn(changedTarget).execute()
    } else {
      changedTarget
    }
  }
}
