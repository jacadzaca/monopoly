package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.createPlayer
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class TilePurchaseTest {
  private val tile = createTile()
  private val buyer = createPlayer()
  private val gameState = mockk<GameState>()
  private val buyersId = UUID.randomUUID()
  private val tileIndex = Random.nextInt()
  private val transformation = TilePurchase(buyer, buyersId, tile, tileIndex)

  @BeforeEach
  fun setUp() {
    val tileSlot = slot<Tile>()
    val buyerSlot = slot<Player>()
    every { gameState.update(tileIndex, capture(tileSlot)) } answers {
      every { gameState.tiles[tileIndex] } returns tileSlot.captured
      gameState
    }
    every { gameState.update(buyersId, capture(buyerSlot)) } answers {
      every { gameState.players[buyersId] } returns buyerSlot.captured
      gameState
    }
  }

  @Test
  fun `apply sets the tile's owner to buyer`() {
    val actual = transformation.apply(gameState)
    assertEquals(buyersId, actual.tiles[tileIndex].ownersId)
  }

  @Test
  fun `apply detracts the tile's price from the buyer's balance`() {
    val detractedBalance = buyer.balance - tile.price
    val actual = transformation.apply(gameState)
    assertEquals(
      detractedBalance,
      actual.players[buyersId]!!.balance
    )
  }
}

