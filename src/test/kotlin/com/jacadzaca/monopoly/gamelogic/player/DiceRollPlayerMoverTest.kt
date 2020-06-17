package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.getTestPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DiceRollPlayerMoverTest {
  companion object {
    const val boardSize = 20
  }
  private val dieRoller = { 5 }
  private val playerMover: DiceRollPlayerMover = DiceRollPlayerMover(dieRoller)

  @Test
  fun `move should add increase toMove position`() {
    val toMove = getTestPlayer()
    val expected = toMove.copy(position = toMove.position + dieRoller())
    val actual = playerMover.move(toMove, boardSize)

    assertEquals(expected.position, actual.position)
  }

  @Test
  fun `move should wrap position`() {
    val toMove = getTestPlayer().copy(position = boardSize - 1)

    val expected = toMove.copy(position = dieRoller() - 1)
    val actual = playerMover.move(toMove, boardSize)
    assertEquals(expected.position, actual.position)
  }
}
