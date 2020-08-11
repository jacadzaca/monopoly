package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class PlayerMovesTest {
  private val player = mockk<Player>(relaxed = true)
  private val gameState = mockk<GameState>(relaxed = true)
  private val playersId = UUID.randomUUID()
  private val newPosition = Random.nextInt(1, Int.MAX_VALUE)
  private val transformation = PlayerMoves(player, playersId, newPosition, gameState)

  @Test
  fun `transform sets player's position to newPosition`() {
    transformation.transform()
    verify { player.setPosition(newPosition) }
  }
}
