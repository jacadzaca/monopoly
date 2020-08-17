package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class CommandTest {
  private val gameState = mockk<GameState>(relaxed = true)
  private val transformation = spyk<Command>()

  @Test
  fun test() {
    every { transformation.execute() } returns gameState
    transformation.apply()
    verify { gameState.addTransformation(transformation) }
  }
}
