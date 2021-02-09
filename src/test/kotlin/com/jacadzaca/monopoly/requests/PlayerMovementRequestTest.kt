package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.MovePlayer
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class PlayerMovementRequestTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val createMove = mockk<(Player, UUID, GameState) -> MovePlayer>()
  private val request = PlayerMovementRequest(playersId, createMove)

  @Test
  fun `validate returns verifiedEvent if the player with given id exists`() {
    every { gameState.players[playersId] } returns player
    val createdMove = mockk<MovePlayer>()
    every { createMove(player, playersId, gameState) } returns createdMove
    assertEquals(Computation.success(createdMove), request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the event references a non-existing player`() {
    every { gameState.players[playersId] } returns null
    assertEquals(INVALID_PLAYER_ID, request.validate(gameState))
  }
}
