package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

data class LeavePlayer(
  val playersId: UUID,
  val target: GameState
) : Command {
  override fun asEvent(): Event = Event.PlayerLeft(playersId)

  override fun execute(): GameState {
    return target.copy(players = target.players.remove(playersId))
  }
}
