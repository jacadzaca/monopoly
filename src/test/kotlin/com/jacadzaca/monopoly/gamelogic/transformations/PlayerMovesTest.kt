package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*

internal class PlayerMovesTest {
  private val player = mockk<Player>(relaxed = true)
  private val gameState = mockk<GameState>(relaxed = true)
  private val playersId = UUID.randomUUID()
  private val newPosition = randomPositive()
  private val createPayment = mockk<(Player, UUID, Player, UUID, BigInteger, GameState) -> (PayLiability)>()
  private val transformation = PlayerMoves(player, playersId, newPosition, gameState, createPayment)

  @BeforeEach
  fun setUp() {
    val tileWithoutOwner = mockk<Tile>()
    every { gameState.tiles[newPosition] } returns tileWithoutOwner
    every { tileWithoutOwner.ownersId } returns null
  }

  @Test
  fun `transform sets player's position to newPosition`() {
    transformation.execute()
    verify { player.setPosition(newPosition) }
  }

  @Test
  fun `transform makes player pay a liability if he steps on a tile owned by different player`() {
    val tileOwnedByOther = mockk<Tile>()
    every { tileOwnedByOther.totalRent() } returns randomPositive().toBigInteger()
    every { tileOwnedByOther.ownersId } returns UUID.randomUUID()
    every { gameState.players[tileOwnedByOther.ownersId] } returns mockk(name = "tile owner")
    every { gameState.tiles[newPosition] } returns tileOwnedByOther
    val rentPayment = mockk<PayLiability>()
    every { rentPayment.apply() } returns gameState
    every {
      createPayment(
        player,
        playersId,
        gameState.players[tileOwnedByOther.ownersId]!!,
        tileOwnedByOther.ownersId!!,
        tileOwnedByOther.totalRent(),
        gameState
      )
    } returns rentPayment
    transformation.execute()
    verify { rentPayment.apply() }
  }
}
