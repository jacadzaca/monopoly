package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState

abstract class Transformation {
  fun apply(): GameState = transform().addTransformation(this)
  internal abstract fun transform(): GameState
}
