package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState

abstract class Command {
  protected abstract fun asEvent(): Event
  internal abstract fun execute(): GameState
  fun apply(): GameState = execute().addRecentEvent(asEvent())
}
