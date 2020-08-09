package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class TransformationTest {
  private val gameState = mockk<GameState>(relaxed = true)
  private val transformation = object : Transformation() {
    override fun transform(): GameState = gameState
  }

  @Test
  fun test() {
    transformation.apply()
    verify { gameState.addTransformation(transformation) }
  }
}

