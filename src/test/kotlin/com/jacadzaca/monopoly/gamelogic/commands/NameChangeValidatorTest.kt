package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.nextString
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.NameChangeValidator
import com.jacadzaca.monopoly.requests.validators.NameChangeValidator.Companion.PLAYER_ALREADY_NAMED
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
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
