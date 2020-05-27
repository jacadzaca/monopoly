package com.jacadzaca.monopoly.gamelogic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class GameBoardImplTest {
  companion object {
    private const val BOARD_SIZE = 20
    private const val ROLLED_MOVE = 5
  }
  private lateinit var gameBoard: GameBoardImpl

  @BeforeEach
  fun setUp() {
    gameBoard =
      GameBoardImpl(BOARD_SIZE) { ROLLED_MOVE }
  }

  @Test
  fun `canPlayerExecuteAction should return false if committer's id and player's id differ`() {
    val player = getTestPlayer()
    val gameAction = getTestGameEvent()
    assertFalse(gameBoard.canPlayerExecuteAction(player, gameAction))
  }

  @Test
  fun `movePlayer should return Player with position increased by whatever dieRoller returns`() {
    val player = getTestPlayer()
    assertEquals(
      player.piece.position + ROLLED_MOVE,
      gameBoard.movePlayer(player).piece.position)
  }

  @Test
  fun `movePlayer should wrap position calculation`() {
    val rolledMove = 3
    gameBoard =
      GameBoardImpl(BOARD_SIZE) { rolledMove }
    val player = getTestPlayer().copy(piece = Piece(BOARD_SIZE - rolledMove))
    val wrappedPosition = 0
    assertEquals(wrappedPosition, gameBoard.movePlayer(player).piece.position)
  }

  @Test
  fun `addFunds should add to player's balance`() {
    val player = getTestPlayer()
    val howMuch = 123.toBigInteger()
    assertEquals(player.balance + howMuch,
                  gameBoard.addFunds(player, howMuch).balance)
  }

  @Test
  fun `detractFunds should detract from player's balance`() {
    val player = getTestPlayer()
    val howMuch = 123.toBigInteger()
    assertEquals(player.balance - howMuch,
                  gameBoard.detractFunds(player, howMuch).balance)
  }

  @Test
  fun `detractFunds should throw an IllegalArgument if user is trying to extract more than the player has`() {
    val player = getTestPlayer()
    val howMuch = player.balance + 123.toBigInteger()
    assertThrows<IllegalArgumentException> {
      gameBoard.detractFunds(player, howMuch)
    }
  }

  private fun getTestGameEvent(): GameEvent =
    GameEvent(UUID.randomUUID(), 1)

  private fun getTestPlayer(): Player =
    Player(
      UUID.randomUUID(),
      Piece(),
      1234.toBigInteger())
}
