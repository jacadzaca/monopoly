package com.jacadzaca.monopoly

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class GameRoomImplTest {
  private val gameLogic = mockk<MonopolyLogic>()
  private lateinit var player: Player
  private lateinit var players: LinkedList<Player>
  private lateinit var gameRoom: GameRoomImpl

  @BeforeEach
  fun init() {
    players = spyk()
    player = TestPlayer()
    players.addAll(listOf(player, TestPlayer()))
    gameRoom = GameRoomImpl(players, gameLogic)
  }

  @Test
  fun addPlayerAppendsToPlayers() {
    val somePlayer = TestPlayer()
    gameRoom.addPlayer(somePlayer)
    verify { players.add(any()) }
  }

  @Test
  fun executeActionMoveInputExecuteMove() {
    val moveAction = "move 1"
    val moveSize = moveAction.split(" ")[1].toInt()
    every { gameLogic.movePiece(moveSize, player.piece) } returns Piece(position = player.piece.position + moveSize)
    assertEquals(Piece(moveSize).toString(), gameRoom.executeAction(moveAction))
  }

  @Test
  fun executeActionWrongInputReturnError() {
    val wrongInput = "asfg"
    val expectedError = "Wrong input"
    assertEquals(expectedError, gameRoom.executeAction(wrongInput))
  }
}
