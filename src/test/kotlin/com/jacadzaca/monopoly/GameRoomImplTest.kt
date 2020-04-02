package com.jacadzaca.monopoly

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import java.util.*

@ExtendWith(MockKExtension::class)
class GameRoomImplTest {
  private val player = mockk<Player>(relaxed = true)
  private val otherPlayer = mockk<Player>(relaxed = true)
  private val gameLogic = mockk<MonopolyLogic>(relaxed = true)
  private lateinit var players: LinkedList<Player>
  private lateinit var gameRoom: GameRoomImpl

  @BeforeEach
  fun init() {
    players = spyk()
    players.addAll(listOf(player, otherPlayer))
    gameRoom = GameRoomImpl(players, gameLogic)
    every { player.piece } returns Piece()
  }

  @Test
  fun addPlayerAppendsToPlayers() {
    gameRoom.addPlayer(player)
    verify { players.add(any()) }
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
