package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState

abstract class Command {
  fun apply(): GameState = execute().addTransformation(this)
  internal abstract fun execute(): GameState
}
