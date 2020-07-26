package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.createPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class TilePurchaseEventApplierTest {
  private val tile = createTile()
  private val buyer = createPlayer()
  private val gameState = mockk<GameState>()
  private val eventApplier = TilePurchaseEventApplier()
  private val event = VerificationResult.VerifiedTilePurchaseEvent(buyer, UUID.randomUUID(), tile, Random.nextInt())

  @BeforeEach
  fun setUp() {
    val tileSlot = slot<Tile>()
    val buyerSlot = slot<Player>()
    every { gameState.update(event.tileIndex, capture(tileSlot)) } answers {
      every { gameState.tiles[event.tileIndex] } returns tileSlot.captured
      gameState
    }
    every { gameState.update(event.buyerId, capture(buyerSlot)) } answers {
      every { gameState.players[event.buyerId] } returns buyerSlot.captured
      gameState
    }
  }

  @Test
  fun `apply sets the tile's owner to buyer`() {
    val tileOwnedByBuyer = tile.copy(owner = event.buyerId)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(tileOwnedByBuyer.owner, actual.tiles[event.tileIndex].owner)
  }

  @Test
  fun `apply detracts the tile's price from the buyer's balance`() {
    val buyerWithDetractedFunds = buyer.copy(balance = buyer.balance - tile.price)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(buyerWithDetractedFunds.balance, actual.players[event.buyerId]!!.balance)
  }
}