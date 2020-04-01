package com.jacadzaca.monopoly

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class GameRoomImplTest {
  private val player = mockk<Player>(relaxed = true)
  private val otherPlayer = mockk<Player>(relaxed = true)
  private val players = LinkedList<Player>()
  private val gameLogic = mockk<MonopolyLogic>(relaxed = true)
  private lateinit var gameRoom: GameRoomImpl

  @BeforeEach
  fun init() {
    players.addAll(listOf(player, otherPlayer))
    gameRoom = GameRoomImpl(players, gameLogic)
    every { player.piece } returns Piece()
  }

  @Test
  fun executeActionMoveInputExecuteMove() {
    val moveAction = "move 1"
    assertEquals(Piece(1).toString(), gameRoom.executeAction(moveAction))
  }

  @Test
  fun executeActionWrongInputReturnError() {
    val wrongInput = "asfg"
    assertEquals("Wrong input", gameRoom.executeAction(wrongInput))
  }
}
