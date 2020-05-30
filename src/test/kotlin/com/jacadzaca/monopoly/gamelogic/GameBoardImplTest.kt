package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.createLiability
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
    rentCalculator = mockk(relaxed = true)
    tiles = mockk()
    gameBoard =
      GameBoardImpl(BOARD_SIZE, { ROLLED_MOVE }, tiles, rentCalculator)
    every { tiles[player.piece.position + ROLLED_MOVE] } returns createTile(player.id)
  }

  @Test
  fun `canPlayerExecuteAction should return false if committer's id and player's id differ`() {
    val gameAction = getTestGameEvent()
    assertFalse(gameBoard.canPlayerExecuteAction(player, gameAction))
  }

  @Test
  fun `movePlayer should return Player with position increased by whatever dieRoller returns`() {
    assertEquals(
      player.piece.position + ROLLED_MOVE,
      gameBoard.movePlayer(player).piece.position)
  }

  @Test
  fun `movePlayer should return Player with a liability if they lands on a tile owned by a different player `() {
    val fieldOwnedByOther = createTile()
    every { tiles[player.piece.position + ROLLED_MOVE] } returns fieldOwnedByOther

    val totalRent = 123.toBigInteger()
    val liabilityTowardsOther = createLiability(fieldOwnedByOther.owner, totalRent)
    every { rentCalculator.getTotalRentFor(fieldOwnedByOther) } returns totalRent

    val movedPlayer = gameBoard.movePlayer(player)
    assertEquals(liabilityTowardsOther, movedPlayer.liability)
    assertNotNull(movedPlayer.liability)
  }

  @Test
  fun `movePlayer should wrap position calculation`() {
    player = player.copy(piece = Piece(BOARD_SIZE - ROLLED_MOVE))
    val wrappedPosition = 0
    every { tiles[wrappedPosition] } returns createTile(player.id)
    assertEquals(wrappedPosition, gameBoard.movePlayer(player).piece.position)
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
