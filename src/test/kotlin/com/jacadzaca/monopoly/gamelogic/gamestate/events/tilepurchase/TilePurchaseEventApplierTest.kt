package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TilePurchaseEventApplierTest {
  private val tile = createTile()
  private val buyer = getTestPlayer()
  private val tileSlot = slot<Tile>()
  private val playerSlot = slot<Player>()
  private val gameState = mockk<GameState>()
  private val updatedGameState = mockk<GameState>()
  private val eventApplier = TilePurchaseEventApplier()
  private val event = VerificationResult.VerifiedTilePurchaseEvent(buyer, buyer.id, tile, 0)

  @BeforeEach
  fun setUp() {
    // WARNING! this  makes the order of update a tested condition - first update the tile, then the player
    every { gameState.update(event.tileIndex, capture(tileSlot)) } returns updatedGameState
    every { updatedGameState.update(event.playerId, capture(playerSlot)) } returns updatedGameState
  }

  @Test
  fun `apply should set the tile's owner to buyer`() {
    val tileOwnedByBuyer = tile.copy(owner = event.playerId)
    val actual = eventApplier.apply(event, gameState)
    every { updatedGameState.tiles[event.tileIndex] } returns tileSlot.captured
    assertEquals(tileOwnedByBuyer.owner, actual.tiles[event.tileIndex].owner)
  }

  @Test
  fun `apply should detract the tile's price from the buyer's balance`() {
    val buyerWithDetractedFunds = buyer.copy(balance = buyer.balance - tile.price)
    val actual = eventApplier.apply(event, gameState)
    every { updatedGameState.players[event.playerId] } returns playerSlot.captured
    assertEquals(buyerWithDetractedFunds.balance, actual.players[buyer.id]!!.balance)
  }
}
