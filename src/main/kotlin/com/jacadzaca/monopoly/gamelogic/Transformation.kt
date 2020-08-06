package com.jacadzaca.monopoly.gamelogic

abstract class Transformation {
  abstract fun apply(target: GameState): GameState
}
