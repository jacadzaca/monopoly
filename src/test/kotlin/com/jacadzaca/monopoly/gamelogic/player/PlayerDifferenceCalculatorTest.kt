package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.getTestPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class PlayerDifferenceCalculatorTest {
  private val boardSize = 20
  private val differenceCalculator = PlayerDifferenceCalculator(boardSize)
  private val subtrahend = getTestPlayer(startPosition = 5)

  @Test
  fun `calculate properly calculates change in position`() {
    val change = boardSize - subtrahend.position
    if (change < 0) {
      throw RuntimeException("change in test case must be >0")
    }
    val minuend = subtrahend.copy(position = subtrahend.position + change)

    val expected = PlayerDifference(changeInPosition = change)
    val actual = differenceCalculator.calculate(minuend, subtrahend)
    assertEquals(expected.changeInPosition, actual.changeInPosition)
  }

  @Test
  fun `calculate should unwrap change in position if minuend's position is less than subtrahend's position`() {
    val minuend = subtrahend.copy(position = subtrahend.position - 1)

    val expected = PlayerDifference(changeInPosition = boardSize + minuend.position)
    val actual = differenceCalculator.calculate(minuend, subtrahend)
    assertEquals(expected.changeInPosition, actual.changeInPosition)
  }

  @Test
  fun `calculate should return positive changeInBalance if funds were added`() {
    val change = 10.toBigInteger()
    val minuend = subtrahend.addFunds(change)

    val expected = PlayerDifference(changeInBalance = change)
    val actual = differenceCalculator.calculate(minuend, subtrahend)

    assertEquals(expected.changeInBalance, actual.changeInBalance)
  }

  @Test
  fun `calculate should return negative changeInBalance if funds were detracted`() {
    val change = 10.toBigInteger()
    val minuend = subtrahend.detractFunds(change)

    val expected = PlayerDifference(changeInBalance = -change)
    val actual = differenceCalculator.calculate(minuend, subtrahend)

    assertEquals(expected.changeInBalance, actual.changeInBalance)
  }

  @Test
  fun `calculate should `() {

  }

  @Test
  fun `calculate throws IllegalArgument if ids differ`() {
    val differentId = UUID.randomUUID()
    val minuend = subtrahend.copy(id = differentId)
    assertThrows<IllegalArgumentException> {
      differenceCalculator.calculate(minuend, subtrahend)
    }
  }
}

