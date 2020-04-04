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
  private lateinit var player: NetworkPlayer
  private lateinit var players: LinkedList<NetworkPlayer>
  private lateinit var inputAllower: InputAllowerImpl
  private lateinit var gameRoom: GameRoomImpl

  @BeforeEach
  fun init() {
    players = spyk()
    player = NetworkPlayer(mockk(), Piece())
    players.addAll(listOf(player))
    inputAllower = mockk(relaxed = true)
    gameRoom = GameRoomImpl(players, gameLogic, inputAllower)
  }

  @Test
  fun addPlayerAppendsToPlayers() {
    gameRoom.addPlayer(player)
    verify { players.add(player) }
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

  @Test
  fun `if game room has no players, addPlayer allows input from added player`() {
    players.clear()
    gameRoom.addPlayer(player)
    verify { inputAllower.allowInput(player, any()) }
  }

  @Test
  fun `if game room has players, addPlayer disallows input from added player`() {
    gameRoom.addPlayer(player)
    verify { inputAllower.disallowInput(player, any()) }
  }

  @Test
  fun `input allowed with executeAction as handler`() {
    players.clear()
    gameRoom.addPlayer(player)
    verify { inputAllower.allowInput(any(), gameRoom::executeAction) }
  }

  @Test
  fun `input disallowed with notYourTurnSupplier as handler`() {
    gameRoom.addPlayer(player)
    verify { inputAllower.disallowInput(any(), gameRoom::notYourTurnSupplier) }
  }
}
