package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class JoinPlayerTest {
    private val gameState = mockk<GameState>()
    private val playersId = UUID.randomUUID()
    private val newPlayer = mockk<Player>()
    private val command = JoinPlayer(newPlayer, playersId, gameState)

    @BeforeEach
    fun setUp() {
        every { gameState.put(playersId, newPlayer) } returns gameState
    }

    @Test
    fun `execute adds player to the GameState`() {
        command.execute()
        verify { gameState.put(playersId, newPlayer) }
    }
}
