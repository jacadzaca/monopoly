package com.jacadzaca.monopoly.gamelogic

abstract class Transformation(private val target: GameState) {
  abstract fun apply(): GameState
}
