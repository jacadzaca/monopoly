package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

class JoinPlayer(
  private val playersId: UUID,
  private val target: GameState
) : Command() {
  private companion object {
    private val newPlayer = Player(position = 0, balance = 1000.toBigInteger())
  }
  override fun asEvent(): Event = Event.PlayerJoined(playersId)

  override fun execute(): GameState {
    return target.put(playersId, newPlayer)
  }
}
