package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState

abstract class Command {
  fun apply(): GameState = execute().addTransformation(this)
  protected abstract fun asEvent(): Event
  internal abstract fun execute(): GameState
}
