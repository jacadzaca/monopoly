package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class PlayerMovesTest {
  private val player = mockk<Player>()
  private val gameState = mockk<GameState>()
  private val rollDice = mockk<() -> Int>()
  private val playersId = UUID.randomUUID()
  private val transformation = PlayerMoves(player, playersId, rollDice)

  @BeforeEach
  fun setUp() {
    val calculatedPositionSlot = slot<Int>()
    // game boards will probably not be THAT big - no need to check for integer overflows etc.
    every { gameState.boardSize } returns Random.nextInt(2, 10_000)
    every { gameState.update(playersId, any()) } returns gameState
    every { gameState.players[playersId] } returns player
    every { rollDice() } returns Random.nextInt(1, gameState.boardSize - 1)
    every { player.position } returns gameState.boardSize - rollDice() - 1
    every { player.copy(position = capture(calculatedPositionSlot)) } answers {
      every { player.position } returns calculatedPositionSlot.captured
      player
    }
  }

  @Test
  fun `apply adds dieRoller result to the player's position`() {
    val previousPosition = player.position
    val expected = previousPosition + rollDice()
    val actual = transformation.apply(gameState)
    assertEquals(
      expected,
      actual.players[playersId]!!.position
    )
  }

  @Test
  fun `apply wraps position calculation`() {
    every { player.position } returns gameState.boardSize - 1
    val expected = rollDice() - 1
    val actual = transformation.apply(gameState)
    assertEquals(
      expected,
      actual.players[playersId]!!.position
    )
  }
}
