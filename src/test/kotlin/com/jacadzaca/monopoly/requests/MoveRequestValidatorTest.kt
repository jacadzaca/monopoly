package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class MoveRequestValidatorTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val createMove = mockk<(Player, UUID, GameState) -> MovePlayer>()
  private val validator = MoveRequestValidator(createMove)

  @Test
  fun `validate returns a success if the player with given id exists`() {
    every { gameState.players[playersId] } returns player
    val createdMove = mockk<MovePlayer>()
    every { createMove(player, playersId, gameState) } returns createdMove
    assertEquals(Computation.success(createdMove), validator.validate(playersId, gameState))
  }

  @Test
  fun `validate returns a failure if the event references a non-existing player`() {
    every { gameState.players[playersId] } returns null
    assertEquals(INVALID_PLAYER_ID, validator.validate(playersId, gameState))
  }
}
