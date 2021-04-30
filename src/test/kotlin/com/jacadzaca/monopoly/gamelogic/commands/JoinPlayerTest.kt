package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.util.*

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
