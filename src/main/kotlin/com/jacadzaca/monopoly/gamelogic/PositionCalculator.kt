package com.jacadzaca.monopoly.gamelogic

import kotlin.random.*

interface PositionCalculator {
  companion object {
    val instance = PositionCalculatorImpl { Random.nextInt(1, 6 + 1) }
  }
  fun calculate(currentPosition: Int, boardSize: Int): Int
}
