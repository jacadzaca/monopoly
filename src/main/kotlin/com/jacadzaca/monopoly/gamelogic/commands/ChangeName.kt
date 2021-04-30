package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

class ChangeName(
  private val playersId: UUID,
  private val name: String,
  private val target: GameState,
) : Command {
  override fun execute(): GameState {
    return target.updatePlayer(playersId, newName = name)
  }
}

