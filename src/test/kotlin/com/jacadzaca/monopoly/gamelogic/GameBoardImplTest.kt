package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createLiability
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.getTestGameEvent
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GameBoardImplTest {
  companion object {
    private const val BOARD_SIZE = 20
    private const val ROLLED_MOVE = 5
  }
  private lateinit var gameBoard: GameBoardImpl
  private lateinit var rentCalculator: RentCalculator
  private lateinit var tiles: List<Tile>
  private lateinit var player: Player

  @BeforeEach
  fun setUp() {
    player = getTestPlayer()
    rentCalculator = mockk()
    tiles = mockk()
    gameBoard =
      GameBoardImpl(BOARD_SIZE, { ROLLED_MOVE }, tiles, rentCalculator)
    every { tiles[any()] } returns createTile(null)
    every { tiles.indexOf(GameBoard.startTile) } returns 0
  }

  @Test
  fun `canPlayerExecuteAction should return false if committer's id and player's id differ`() {
    val gameAction = getTestGameEvent()
    assertFalse(gameBoard.canPlayerExecuteAction(player, gameAction))
  }

  @Test
  fun `movePlayer should return Player with position increased by whatever dieRoller returns`() {
    val expectedPosition = createTile()
    every { tiles[tiles.indexOf(player.piece.position) + ROLLED_MOVE] } returns expectedPosition
    assertSame(expectedPosition, gameBoard.movePlayer(player).piece.position)
  }

  @Test
  fun `movePlayer should wrap position calculation`() {
    every { tiles[0] } returns GameBoard.startTile
    player = player.copy(piece = Piece(position = tiles[BOARD_SIZE - ROLLED_MOVE]))
    every { tiles.indexOf(player.piece.position) } returns BOARD_SIZE - ROLLED_MOVE
    assertSame(GameBoard.startTile, gameBoard.movePlayer(player).piece.position)
  }

  @Test
  fun `collectRent should return a Player with a liability if they lands on a tile owned by a different player `() {
    val fieldOwnedByOther = createTile()
    player = player.copy(piece = player.piece.copy(position = fieldOwnedByOther))

    val totalRent = 123.toBigInteger()
    val liabilityTowardsOther = createLiability(fieldOwnedByOther.owner!!, totalRent)
    every { rentCalculator.getTotalRentFor(fieldOwnedByOther) } returns totalRent

    val playerWithLiability = gameBoard.collectRent(player)
    assertEquals(liabilityTowardsOther, playerWithLiability.liability)
    assertNotNull(playerWithLiability.liability)
  }

  @Test
  fun `collectRent should not return a Player with a liability if they lands on a tile owned by them`() {
    val fieldOwnedByPlayer = createTile(player.id)
    player = player.copy(piece = player.piece.copy(position = fieldOwnedByPlayer))
    assertNull(gameBoard.collectRent(player).liability)
  }

  @Test
  fun `buyTile should update the tile's owner`() {
    val boughtTile = player.piece.position.copy(owner = player.id)
    assertEquals(boughtTile, gameBoard.buyTile(player).piece.position)
  }

  @Test
  fun `buyTile should throws IllegalArgument if player has insufficient funds`() {
    player = player.copy(piece = Piece(position = createTile().copy(price = player.balance + 1.toBigInteger())))
    assertThrows<IllegalArgumentException> {
      gameBoard.buyTile(player)
    }
  }

  @Test
  fun `buyTile should throws IllegalArgument if player already own the tile`() {
    player = player.copy(piece = Piece(position = createTile(player.id)))
    assertThrows<IllegalArgumentException> {
      gameBoard.buyTile(player)
    }
  }

  @Test
  fun `addFunds should add to player's balance`() {
    val howMuch = 123.toBigInteger()
    assertEquals(player.balance + howMuch,
                  gameBoard.addFunds(player, howMuch).balance)
  }

  @Test
  fun `addFunds should throw IllegalArgument if howMuch is negative`() {
    val howMuch = (-123).toBigInteger()
    assertThrows<IllegalArgumentException> {
      gameBoard.addFunds(player, howMuch)
    }
  }

  @Test
  fun `detractFunds should thor IllegalArgument if howMuch is negative`() {
    val howMuch = (-123).toBigInteger()
    assertThrows<IllegalArgumentException> {
      gameBoard.detractFunds(player, howMuch)
    }
  }

  @Test
  fun `detractFunds should detract from player's balance`() {
    val howMuch = 123.toBigInteger()
    assertEquals(player.balance - howMuch,
                  gameBoard.detractFunds(player, howMuch).balance)
  }

  @Test
  fun `detractFunds should throw an IllegalArgument if user is trying to extract more than the player has`() {
    val howMuch = player.balance + 123.toBigInteger()
    assertThrows<IllegalArgumentException> {
      gameBoard.detractFunds(player, howMuch)
    }
  }
}
