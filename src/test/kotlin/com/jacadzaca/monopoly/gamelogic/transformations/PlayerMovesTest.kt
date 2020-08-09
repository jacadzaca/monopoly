package com.jacadzaca.monopoly.gamelogic.transformations

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
  private val player = mockk<Player>(relaxed = true)
  private val gameState = mockk<GameState>()
  private val playersId = UUID.randomUUID()
  private val boardSize = Random.nextInt(2, 10_000)
  private val moveBy = Random.nextInt(1, boardSize - 1)
  private val transformation =
    PlayerMoves(
      player,
      playersId,
      moveBy,
      gameState
    )

  @BeforeEach
  fun setUp() {
    val calculatedPositionSlot = slot<Int>()
    every { gameState.boardSize } returns boardSize
    every { gameState.update(playersId, any()) } returns gameState
    every { gameState.addTransformation(any()) } returns gameState
    every { gameState.players[playersId] } returns player
    every { player.copy(position = capture(calculatedPositionSlot)) } answers {
      every { player.position } returns calculatedPositionSlot.captured
      player
    }
  }

  @Test
  fun `transform adds moveBy to the player's position`() {
    every { player.position } returns gameState.boardSize - transformation.moveBy - 1
    val previousPosition = player.position
    val actual = transformation.transform().players[playersId]!!.position
    assertEquals(previousPosition + transformation.moveBy, actual)
  }

  @Test
  fun `transform wraps the position calculation`() {
    every { player.position } returns gameState.boardSize - 1
    val actual = transformation.transform().players[playersId]!!.position
    assertEquals(transformation.moveBy - 1, actual)
  }
}
