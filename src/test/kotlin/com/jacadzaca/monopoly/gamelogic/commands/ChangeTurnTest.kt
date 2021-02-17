package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.*

internal class ChangeTurnTest {
  private val gameState = mockk<GameState>()
  private val command = ChangeTurn(gameState)
  private val newTurn = slot<Int>()

  @BeforeEach
  fun setUp() {
    every { gameState.turnOrder.size } returns Random.nextPositive(from = 1)
    every { gameState.copy(currentTurn = capture(newTurn)) } returns gameState
  }

  @Test
  fun `execute increments currentTurn`() {
    every { gameState.currentTurn } returns Random.nextPositive(until = gameState.turnOrder.size - 1)
    val oldTurn = gameState.currentTurn
    command.execute()
    every { gameState.currentTurn } returns newTurn.captured
    assertEquals(oldTurn + 1, command.execute().currentTurn)
  }

  @Test
  fun `execute wraps the calculation`() {
    every { gameState.currentTurn } returns gameState.turnOrder.size - 1
    command.execute()
    every { gameState.currentTurn } returns newTurn.captured
    assertEquals(0, command.execute().currentTurn)
  }
}
