package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.MovePlayer
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.Request.Companion.NOT_PLAYERS_TURN
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

internal class PlayerMovementRequestTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val createdMove = mockk<MovePlayer>()
  private val createMove = mockk<(Player, UUID, GameState) -> MovePlayer>()
  private val request = PlayerMovementRequest(createMove)

  @BeforeEach
  fun setUp() {
    every { gameState.isPlayersTurn(playersId) } returns true
    every { gameState.players[playersId] } returns player
  }

  @Test
  fun `validate returns verifiedEvent if the player with given id exists`() {
    every { createMove(player, playersId, gameState) } returns createdMove
    assertEquals(Computation.success(createdMove), request.validate(playersId, gameState))
  }

  @Test
  fun `validate returns Failure if the event references a non-existing player`() {
    every { gameState.players[playersId] } returns null
    assertEquals(INVALID_PLAYER_ID, request.validate(playersId, gameState))
  }

  @Test
  fun `validate returns Failure if it is not the player's turn`() {
    every { gameState.isPlayersTurn(playersId) } returns false
    assertEquals(NOT_PLAYERS_TURN, request.validate(playersId, gameState))
  }
}
