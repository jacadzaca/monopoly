package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class MoveEventApplierTest {
  private val player = getTestPlayer()
  private val gameState = mockk<GameState>()
  private val playerMover = mockk<PlayerMover>()
  private val eventApplier = MoveEventApplier(playerMover)
  private val event = VerificationResult.VerifiedMoveEvent(player, player.id)

  @BeforeEach
  fun setUp() {
    val playerSlot = slot<Player>()
    every { gameState.boardSize } returns 20
    every { gameState.update(event.playerId, capture(playerSlot)) } answers {
      every { gameState.players[event.playerId] } returns playerSlot.captured
      gameState
    }
  }

  @Test
  fun `apply should update the mover's position`() {
    val movedPlayer = player.copy(position = Random.nextInt())
    every { playerMover.move(event.player, gameState.boardSize) } returns movedPlayer
    val actual = eventApplier.apply(event, gameState)
    assertEquals(movedPlayer.position, actual.players[event.playerId]!!.position)
  }
}
