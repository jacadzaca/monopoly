package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.createLiability
import com.jacadzaca.monopoly.getTestPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class PlayerDeltaCalculatorTest {
  private val boardSize = 20
  private val deltaCalculator = PlayerDeltaCalculator(boardSize)
  private val previous = getTestPlayer(startPosition = 5)

  @Test
  fun `calculate properly calculates change in position`() {
    val change = boardSize - previous.position
    if (change < 0) {
      throw RuntimeException("change in test case must be >0")
    }
    val current = previous.copy(position = previous.position + change)

    val expected = PlayerDelta(changeInPosition = change)
    val actual = deltaCalculator.calculate(current, previous)
    assertEquals(expected.changeInPosition, actual.changeInPosition)
  }

  @Test
  fun `calculate should unwrap change in position if current's position is less than previous's position`() {
    val current = previous.copy(position = previous.position - 1)

    val expected = PlayerDelta(changeInPosition = boardSize - (previous.position - current.position))
    val actual = deltaCalculator.calculate(current, previous)
    assertEquals(expected.changeInPosition, actual.changeInPosition)
  }

  @Test
  fun `calculate should set changeInPosition to boardSize if current's position is equal to previous'`() {
    val current = previous.copy(position = previous.position)

    val expected = PlayerDelta(changeInPosition = boardSize)
    val actual = deltaCalculator.calculate(current, previous)
    assertEquals(expected.changeInPosition, actual.changeInPosition)
  }

  @Test
  fun `calculate should return positive changeInBalance if funds were added`() {
    val change = 10.toBigInteger()
    val current = previous.addFunds(change)

    val expected = PlayerDelta(changeInBalance = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInBalance, actual.changeInBalance)
  }

  @Test
  fun `calculate should return negative changeInBalance if funds were detracted`() {
    val change = 10.toBigInteger()
    val current = previous.detractFunds(change)

    val expected = PlayerDelta(changeInBalance = -change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInBalance, actual.changeInBalance)
  }

  @Test
  fun `calculate should add a liability if current has one and previous dose not`() {
    val change = createLiability(previous.id, 10.toBigInteger())
    val current = previous.copy(liability = change)

    val expected = PlayerDelta(changeInLiability = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInLiability, actual.changeInLiability)
  }

  @Test
  fun `calculate throws IllegalArgument if ids differ`() {
    val differentId = UUID.randomUUID()
    val current = previous.copy(id = differentId)
    assertThrows<IllegalArgumentException> {
      deltaCalculator.calculate(current, previous)
    }
  }
}

