package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class LeavePlayerTest {
    private val gameState = mockk<GameState>()
    private val playersId = UUID.randomUUID()
    private val leaveReason = "reason"
    private val command = LeavePlayer(playersId, leaveReason, gameState)

    @BeforeEach
    fun setUp() {
        every { gameState.isPlayersTurn(any()) } returns false
        every { gameState.remove(any(), any()) } returns gameState
        every { gameState.disownPlayer(any()) } returns gameState
    }

    @Test
    fun `execute removes player from the player's list`() {
        assertEquals(gameState, command.execute())
        verify { gameState.remove(playersId, leaveReason) }
    }

    @Test
    fun `execute disowns player of all their holdings`() {
        assertEquals(gameState, command.execute())
        verify { gameState.disownPlayer(playersId) }
    }

    @Test
    fun `execute changes turn if the leaving player did not end his turn`() {
        mockkConstructor(ChangeTurn::class)
        every { anyConstructed<ChangeTurn>().execute() } returns gameState
        every { gameState.isPlayersTurn(playersId) } returns true
        assertEquals(gameState, command.execute())
    }
}
