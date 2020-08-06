package com.jacadzaca.monopoly.gamelogic

abstract class Transformation {
  fun apply(): GameState = transform().addTransformation(this)
  internal abstract fun transform(): GameState
}
