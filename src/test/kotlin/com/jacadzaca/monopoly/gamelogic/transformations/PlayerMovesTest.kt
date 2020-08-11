package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Liability
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class PlayerMovesTest {
  private val player = mockk<Player>(relaxed = true)
  private val gameState = mockk<GameState>(relaxed = true)
  private val playersId = UUID.randomUUID()
  private val newPosition = Random.nextInt(1, Int.MAX_VALUE)
  private val createPayment = mockk<(Player, UUID, Liability, GameState) -> (LiabilityPayment)>()
  private val transformation = PlayerMoves(player, playersId, newPosition, gameState, createPayment)

  @BeforeEach
  fun setUp() {
    val tileWithoutOwner = mockk<Tile>()
    every { gameState.tiles[newPosition] } returns tileWithoutOwner
    every { tileWithoutOwner.ownersId } returns null
  }

  @Test
  fun `transform sets player's position to newPosition`() {
    transformation.transform()
    verify { player.setPosition(newPosition) }
  }

  @Test
  fun `transform makes player pay a liability if he steps on a tile owned by different player`() {
    val tileOwnedByOther = mockk<Tile>()
    every { tileOwnedByOther.totalRent() } returns Random.nextInt().toBigInteger()
    every { tileOwnedByOther.ownersId } returns UUID.randomUUID()
    every { tileOwnedByOther.ownersId == playersId } returns false
    every { tileOwnedByOther.ownersId == null } returns false
    every { gameState.players[tileOwnedByOther.ownersId] } returns mockk(name = "tile owner")
    every { gameState.tiles[newPosition] } returns tileOwnedByOther
    val liability = Liability(tileOwnedByOther.totalRent(), gameState.players[tileOwnedByOther.ownersId]!!, tileOwnedByOther.ownersId!!)
    val rentPayment = mockk<LiabilityPayment>()
    every { rentPayment.transform() } returns gameState
    every { createPayment(player, playersId, liability, gameState) } returns rentPayment
    transformation.transform()
    verify { rentPayment.transform() }
  }

}
