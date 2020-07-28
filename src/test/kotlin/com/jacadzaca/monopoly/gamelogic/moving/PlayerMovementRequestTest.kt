package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class PlayerMovementRequestTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val request = PlayerMovementRequest(playersId)

  @Test
  fun `validate returns verifiedEvent if the player with given id exists`() {
    every { gameState.players[playersId] } returns player
    assertEquals(
      ValidationResult.Success(PlayerMoves.create(player, playersId)),
      request.validate(gameState)
    )
  }

  @Test
  fun `validate returns Failure if the event references a non-existing player`() {
    every { gameState.players[playersId] } returns null
    assertEquals(
      ValidationResult.Failure(Request.invalidPlayerId),
      request.validate(gameState)
    )
  }
}
