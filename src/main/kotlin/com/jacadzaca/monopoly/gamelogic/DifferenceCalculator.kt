package com.jacadzaca.monopoly.gamelogic

interface DifferenceCalculator<T> {
  /**
   * @throws IllegalArgumentException if @minuend's and subtrahend's ids differ
   * @return the 'transition' from @subtrahend to @minuend
   */
  fun calculate(minuend: T, subtrahend: T): Difference
}
