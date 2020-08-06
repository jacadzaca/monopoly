package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.transformations.PlayerMoves
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class PlayerMovementRequestTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val actionCreator = mockk<(Player, UUID, GameState) -> PlayerMoves>()
  private val request =
    PlayerMovementRequest(playersId, actionCreator, gameState)

  @Test
  fun `validate returns verifiedEvent if the player with given id exists`() {
    every { gameState.players[playersId] } returns player
    val createdAction = mockk<PlayerMoves>(name = "created PlayerMoves")
    every { actionCreator(player, playersId, gameState) } returns createdAction
    assertEquals(
      ValidationResult.Success(createdAction),
      request.validate()
    )
  }

  @Test
  fun `validate returns Failure if the event references a non-existing player`() {
    every { gameState.players[playersId] } returns null
    assertEquals(
      ValidationResult.Failure(Request.invalidPlayerId),
      request.validate()
    )
  }
}
