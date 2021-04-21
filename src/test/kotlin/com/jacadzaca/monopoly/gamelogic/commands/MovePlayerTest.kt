package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class MovePlayerTest {
  private val player = mockk<Player>()
  private val gameState = mockk<GameState>(relaxed = true)
  private val playersId = UUID.randomUUID()
  private val newPosition = Random.nextPositive()
  private val onBankrupcy = mockk<Command>()
  private val createPayment = mockk<(Player, UUID, Player, UUID, BigInteger, Command, GameState) -> (PayLiability)>()
  private val command = MovePlayer(player, playersId, newPosition, gameState, createPayment, onBankrupcy)

  @BeforeEach
  fun setUp() {
    val tileWithoutOwner = mockk<Tile>()
    every { gameState.tiles[newPosition] } returns tileWithoutOwner
    every { tileWithoutOwner.ownersId } returns null
  }

  @Test
  fun `transform sets player's position to newPosition`() {
    command.execute()
    verify { gameState.updatePlayer(playersId, newPosition) }
  }

  @Test
  fun `transform makes player pay a liability if he steps on a tile owned by different player`() {
    val tileOwnedByOther = mockk<Tile>()
    every { tileOwnedByOther.totalRent() } returns Random.nextPositive().toBigInteger()
    every { tileOwnedByOther.ownersId } returns UUID.randomUUID()
    every { gameState.players[tileOwnedByOther.ownersId] } returns mockk(name = "tile owner")
    every { gameState.tiles[newPosition] } returns tileOwnedByOther
    val rentPayment = mockk<PayLiability>()
    every { rentPayment.execute() } returns gameState
    every {
      createPayment(
        player,
        playersId,
        gameState.players[tileOwnedByOther.ownersId]!!,
        tileOwnedByOther.ownersId!!,
        tileOwnedByOther.totalRent(),
        onBankrupcy,
        gameState,
      )
    } returns rentPayment
    command.execute()
    verify { rentPayment.execute() }
  }
}

