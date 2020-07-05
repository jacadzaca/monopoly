package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class ApplyEventTest {
  private val event = mockk<GameEvent>()
  private val gameState = mockk<GameState>()
  private val gameStateWithChange = mockk<GameState>()

  @BeforeEach
  fun setUp() {
    every { event.apply(any(), any()) } returns gameState
    every { gameState.addChange(event) } returns gameStateWithChange
  }

  @ParameterizedTest
  @ArgumentsSource(GameStateManagerProvider::class)
  fun `applyEvent should add event to gameState's changes`(gameStateManager: GameStateManager) {
    gameStateManager.applyEvent(event, gameState)
    verify { event.apply(any(), gameStateWithChange) }
  }

  @ParameterizedTest
  @ArgumentsSource(GameStateManagerProvider::class)
  fun `applyEvent should call event apply with gameStateManager`(gameStateManager: GameStateManager) {
    gameStateManager.applyEvent(event, gameState)
    verify { event.apply(gameStateManager, any()) }
  }

  private class GameStateManagerProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(GameStateManagerImpl(mockk(), mockk(), mockk()))
      )
    }
  }
}
