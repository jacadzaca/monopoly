package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState

abstract class Command {
  fun apply(): GameState = execute().addTransformation(this)
  internal abstract fun execute(): GameState
}
