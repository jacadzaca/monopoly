package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.nextPositive
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class PositionCalculatorImplTest {
    // until is specified in order to mitigate overflows
    private val diceRoll = Random.nextPositive(until = 10_0000)
    private val positionCalculator = PositionCalculatorImpl { diceRoll }

    @Test
    fun `calculate adds diceRoll to current position`() {
        val currentPosition = Random.nextPositive(until = diceRoll)
        val boardSize = Random.nextPositive(from = currentPosition + diceRoll + 1)
        assertEquals(currentPosition + diceRoll, positionCalculator.calculate(currentPosition, boardSize))
    }

    @Test
    fun `calculate wraps the calculation`() {
        val boardSize = Random.nextPositive(from = diceRoll + 1)
        val currentPosition = boardSize - diceRoll
        assertEquals(0, positionCalculator.calculate(currentPosition, boardSize))
    }
}
