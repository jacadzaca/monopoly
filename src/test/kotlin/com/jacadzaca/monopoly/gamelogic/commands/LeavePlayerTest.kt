package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class LeavePlayerTest {
  private val gameState = mockk<GameState>()
  private val playersId = UUID.randomUUID()
  private val command = LeavePlayer(playersId, gameState)

  @BeforeEach
  fun setUp() {
    every { gameState.isPlayersTurn(any()) } returns false
    every { gameState.remove(any()) } returns gameState
    every { gameState.disownPlayer(any()) } returns gameState
  }

  @Test
  fun `execute removes player from the player's list`() {
    assertEquals(gameState, command.execute())
    verify { gameState.remove(playersId) }
  }

  @Test
  fun `execute disowns player of all their holdings`() {
    assertEquals(gameState, command.execute())
    verify { gameState.disownPlayer(playersId) }
  }

  @Test
  fun `execute changes turn if the leaving player did not end his turn`() {
    mockkConstructor(ChangeTurn::class)
    every { anyConstructed<ChangeTurn>().apply() } returns gameState
    every { gameState.isPlayersTurn(playersId) } returns true
    assertEquals(gameState, command.execute())
  }
}
