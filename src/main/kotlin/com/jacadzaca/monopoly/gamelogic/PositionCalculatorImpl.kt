package com.jacadzaca.monopoly.gamelogic

class PositionCalculatorImpl(private val rollDice: () -> Int) : PositionCalculator {
  override fun calculate(currentPosition: Int, boardSize: Int): Int = (currentPosition + rollDice()) % boardSize
}
