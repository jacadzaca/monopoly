package com.jacadzaca.monopoly.gamelogic

interface Transformation {
  fun apply(target: GameState): GameState
}
