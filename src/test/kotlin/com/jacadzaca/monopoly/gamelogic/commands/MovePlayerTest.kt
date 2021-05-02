package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class MovePlayerTest {
  private val player = mockk<Player>()
  private val gameState = mockk<GameState>(relaxed = true)
  private val playersId = UUID.randomUUID()
  private val newPosition = Random.nextPositive()
  private val onEnterForeignTile = mockk<Lazy<Command>>()
  private val onPassStart = mockk<Lazy<Command>>()
  private val command = MovePlayer(player, playersId, newPosition, gameState, onEnterForeignTile, onPassStart)

  @BeforeEach
  fun setUp() {
    val tileWithoutOwner = mockk<Tile>()
    every { gameState.tiles[newPosition] } returns tileWithoutOwner
    every { tileWithoutOwner.ownersId } returns null
    every { gameState.executeCommandIf(false, any()) } returns gameState
    every { onEnterForeignTile.value } returns mockk<Command>("some command")
    every { player.position } returns newPosition - 1
  }

  @Test
  fun `execute sets player's position to newPosition`() {
    command.execute()
    verify { gameState.updatePlayer(playersId, newPosition) }
  }

  @Test
  fun `execute invokes onEnterForeignTile if player steps on a tile owned by different player`() {
    val tileOwnedByOther = mockk<Tile>()
    every { tileOwnedByOther.totalRent() } returns Random.nextPositive().toBigInteger()
    every { tileOwnedByOther.ownersId } returns UUID.randomUUID()
    every { gameState.players[tileOwnedByOther.ownersId] } returns mockk(name = "tile owner")
    every { gameState.tiles[newPosition] } returns tileOwnedByOther
    command.execute()
    verify { gameState.executeCommandIf(true, onEnterForeignTile) }
  }

  @Test
  fun `execute invokes onPassStart when player passes a start`() {
    every { player.position } returns newPosition + 1
    command.execute()
    verify { gameState.executeCommandIf(true, onPassStart) }
  }
}

