package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Transformation
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class PlayerMovesTest {
  private val player = mockk<Player>(relaxed = true)
  private val gameState = mockk<GameState>()
  private val rollDice = mockk<() -> Int>()
  private val playersId = UUID.randomUUID()
  private val action = PlayerMoves(player, playersId, rollDice)

  @BeforeEach
  fun setUp() {
    val calculatedPositionSlot = slot<Int>()
    // game boards will probably not be THAT big - no need to check for integer overflows etc.
    every { gameState.boardSize } returns Random.nextInt(2, 10_000)
    every { gameState.update(playersId, any()) } returns gameState
    every { gameState.addTransformation(any()) } returns gameState
    every { gameState.players[playersId] } returns player
    every { rollDice() } returns Random.nextInt(1, gameState.boardSize - 1)
    every { player.copy(position = capture(calculatedPositionSlot)) } answers {
      every { player.position } returns calculatedPositionSlot.captured
      player
    }
  }

  @Test
  fun `apply adds rollDice's result to the player's position`() {
    every { player.position } returns gameState.boardSize - rollDice() - 1
    val previousPosition = player.position
    val actual = action.apply(gameState).players[playersId]!!.position
    assertEquals(previousPosition + rollDice(), actual)
  }

  @Test
  fun `apply wraps the position calculation`() {
    every { player.position } returns gameState.boardSize - 1
    val actual = action.apply(gameState).players[playersId]!!.position
    assertEquals(rollDice() - 1, actual)
  }

  @Test
  fun `apply adds PlayerMovement transformation`() {
    val transformation = Transformation.PlayerMovement(playersId, rollDice())
    action.apply(gameState)
    verify { gameState.addTransformation(transformation) }
  }

  @Test
  fun `rollDice's randomness is taken into account when adding transformation`() {
    val transformation = Transformation.PlayerMovement(playersId, Random.nextInt())
    every { rollDice() } returnsMany listOf(transformation.movedBy, 3)
    action.apply(gameState)
    verify { gameState.addTransformation(transformation) }
  }
}
