package com.jacadzaca.monopoly.gamelogic

interface DeltaCalculator<T> {
  /**
   * @throws IllegalArgumentException if @minuend's and subtrahend's ids differ
   * @return the 'transition' from @previous to @current
   */
  fun calculate(current: T, previous: T): Delta
}
