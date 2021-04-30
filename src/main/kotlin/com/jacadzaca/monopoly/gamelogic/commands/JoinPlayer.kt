package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

class JoinPlayer(
  private val newPlayer: Player,
  private val playersId: UUID,
  private val target: GameState
) : Command {
  override fun execute(): GameState {
    return target.put(playersId, newPlayer)
  }
}
