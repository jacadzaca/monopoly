package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MoveEventApplierTest {
  private val boardSize = 20
  private val player = getTestPlayer()
  private val gameState = mockk<GameState>()
  private val updateGameState = mockk<GameState>()
  private val playerMover = mockk<PlayerMover>()
  private val eventApplier = MoveEventApplier(playerMover)
  private val event = VerificationResult.VerifiedMoveEvent(player, player.id)

  @BeforeEach
  fun setUp() {
    every { gameState.boardSize } returns boardSize
    every { gameState.update(event.playerId, any()) } returns updateGameState
  }

  @Test
  fun `apply should update the mover's position`() {
    val movedPlayer = player.copy(position = 10)
    every { playerMover.move(event.player, boardSize) } returns movedPlayer
    eventApplier.apply(event, gameState)
    assertEquals(updateGameState, gameState.update(event.playerId, movedPlayer))
  }
}
