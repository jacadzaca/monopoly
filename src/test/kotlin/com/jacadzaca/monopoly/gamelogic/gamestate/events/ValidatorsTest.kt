package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.tileExists
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


internal class ValidatorsTest {
  companion object {
    private const val boardSize = 20
  }
  private val gameState: GameState = mockk()

  @BeforeEach
  private fun setUp() {
    every { gameState.boardSize } returns boardSize
  }

  @ParameterizedTest
  @ValueSource(ints = [-1, boardSize, boardSize + 1])
  fun `tileExists returns false when the tileIndex is out of bounds for boardSize`(tileIndex: Int) {
    assertFalse(tileExists(tileIndex, gameState))
  }
}
