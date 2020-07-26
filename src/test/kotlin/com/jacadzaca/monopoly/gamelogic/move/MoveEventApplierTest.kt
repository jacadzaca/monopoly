package com.jacadzaca.monopoly.gamelogic.move

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.VerificationResult
import com.jacadzaca.monopoly.gamelogic.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class MoveEventApplierTest {
  private val player = mockk<Player>()
  private val gameState = mockk<GameState>()
  private val rollDice = mockk<() -> Int>()
  private val eventApplier = MoveEventApplier(rollDice)
  private val event = VerificationResult.VerifiedMoveEvent(player, UUID.randomUUID())

  @BeforeEach
  fun setUp() {
    val calculatedPositionSlot = slot<Int>()
    every { gameState.boardSize } returns 20
    every { gameState.update(event.moverId, any()) } returns gameState
    every { gameState.players[event.moverId] } returns player
    every { rollDice() } returns Random.nextInt(1, gameState.boardSize - 1)
    every { player.position } returns gameState.boardSize - rollDice() - 1
    every { player.copy(position = capture(calculatedPositionSlot)) } answers {
      every { player.position } returns calculatedPositionSlot.captured
      player
    }
  }

  @Test
  fun `apply add dieRoller to the player's position`() {
    val previousPosition = player.position
    val expected = previousPosition + rollDice()
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      expected,
      actual.players[event.moverId]!!.position
    )
  }

  @Test
  fun `apply wraps position calculation`() {
    every { player.position } returns gameState.boardSize - 1
    val expected = rollDice() - 1
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      expected,
      actual.players[event.moverId]!!.position
    )
  }
}
