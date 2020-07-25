package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.createPlayer
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

internal class MoveEventVerifierTest {
  private val player = createPlayer()
  private val gameState = mockk<GameState>()
  private val event = GameEvent.MoveEvent(UUID.randomUUID())
  private val verifiedEvent =
      VerificationResult.VerifiedMoveEvent(
          player,
          event.moverId
      )
  private val eventVerifier = MoveEventVerifier()

  @Test
  fun `verify returns verifiedEvent if the player with given id exists`() {
    every { gameState.players[event.moverId] } returns player
    assertEquals(verifiedEvent, eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns Failure if the event references a non-existing player`() {
    every { gameState.players[event.moverId] } returns null
    assertEquals(
        VerificationResult.Failure(
            GameEventVerifier.invalidPlayerId
        ),
      eventVerifier.verify(event, gameState)
    )
  }
}
