package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.validators.*
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.NameChangeValidator.Companion.PLAYER_ALREADY_NAMED
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class NameChangeValidatorTest {
  private val player = mockk<Player>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val createNameChange = mockk<(UUID, String, GameState) -> ChangeName>()
  private val action = mockk<PlayerAction.NameChangeAction>()
  private val newName = Random.nextString()
  private val validator = NameChangeValidator(createNameChange)

  @BeforeEach
  fun setUp() {
    every { gameState.players[playersId] } returns player
    every { action.newName } returns newName
    every { player.name } returns null
  }

  @Test
  fun `validate returns Success if the player's name is not set`() {
    val nameChange = mockk<ChangeName>()
    every { createNameChange(playersId, newName, gameState) } returns nameChange
    assertEquals(Computation.success(nameChange), validator.validate(playersId, action, gameState))
  }

  @Test
  fun `validate returns Failure if the player is not present in game`() {
    every { gameState.players[playersId] } returns null
    assertEquals(INVALID_PLAYER_ID, validator.validate(playersId, action, gameState))
  }

  @Test
  fun `validate returns Failure if the player's name is set`() {
      every { player.name } returns "someName"
      assertEquals(PLAYER_ALREADY_NAMED, validator.validate(playersId, action, gameState))
  }
}

