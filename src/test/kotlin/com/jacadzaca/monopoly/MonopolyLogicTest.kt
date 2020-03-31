package com.jacadzaca.monopoly

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class MonopolyLogicTest {
  private val gameBoard = MonopolyLogicImpl()


  @Test
  fun movePieceShouldIncreasePosition() {
    val tilesCount = 1
    val startingPosition = 0
    val pieceToMove = Piece(startingPosition)
    val movedPiece = Piece(startingPosition + tilesCount)
    assertEquals(movedPiece, gameBoard.movePiece(tilesCount, pieceToMove))
  }
}
