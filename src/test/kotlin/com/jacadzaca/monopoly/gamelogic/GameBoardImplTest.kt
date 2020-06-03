package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createLiability
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestGameEvent
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigInteger

@ExtendWith(MockKExtension::class)
internal class GameBoardImplTest {
  companion object {
    private const val BOARD_SIZE = 20
    private const val ROLLED_MOVE = 5
  }
  private lateinit var gameBoard: GameBoardImpl
  private lateinit var tiles: List<Tile>
  private lateinit var player: Player

  @BeforeEach
  fun setUp() {
    player = getTestPlayer()
    tiles = mockk()
    gameBoard =
      GameBoardImpl(BOARD_SIZE, { ROLLED_MOVE }, tiles)
    every { tiles[any()] } returns createTile(null)
    every { tiles.indexOf(GameBoard.startTile) } returns 0
  }

  @Test
  fun `canPlayerExecuteAction should return false if committer's id and player's id differ`() {
    val gameAction = getTestGameEvent()
    assertFalse(gameBoard.canPlayerExecuteAction(player, gameAction))
  }

  @Test
  fun `collectRent should return a Player with a liability if they lands on a tile owned by a different player `() {
    val fieldOwnedByOther = createTile().copy(buildings = listOf(Building(122.toBigInteger()), Building(BigInteger.ONE)))
    player = player.copy(position = fieldOwnedByOther)

    val totalRent = 123.toBigInteger()
    val liabilityTowardsOther = createLiability(fieldOwnedByOther.owner!!, totalRent)

    val playerWithLiability = gameBoard.collectRent(player)
    assertEquals(liabilityTowardsOther, playerWithLiability.liability)
    assertNotNull(playerWithLiability.liability)
  }

  @Test
  fun `collectRent should not return a Player with a liability if they lands on a tile owned by them`() {
    val fieldOwnedByPlayer = createTile(player.id)
    player = player.copy(position = fieldOwnedByPlayer)
    assertNull(gameBoard.collectRent(player).liability)
  }
}
