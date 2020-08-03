package com.jacadzaca.monopoly.gamelogic

interface Action {
  fun apply(target: GameState): GameState
}
