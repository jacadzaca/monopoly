package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class TransformationTest {
  private val gameState = mockk<GameState>(relaxed = true)
  private val transformation = spyk<Transformation>()

  @Test
  fun test() {
    every { transformation.transform() } returns gameState
    transformation.apply()
    verify { gameState.addTransformation(transformation) }
  }
}

