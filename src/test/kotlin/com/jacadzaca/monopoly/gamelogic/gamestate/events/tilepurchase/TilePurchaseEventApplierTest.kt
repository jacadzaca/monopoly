package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TilePurchaseEventApplierTest {
  private val buyer = getTestPlayer()
  private val tile = createTile()
  private val eventApplier = TilePurchaseEventApplier()
  private val gameState = mockk<GameState>()
  private lateinit var updatedGameState: GameState
  private val event = VerificationResult.VerifiedTilePurchaseEvent(buyer, buyer.id, tile, 0)

  @BeforeEach
  fun setUp() {
    updatedGameState = mockk()
    every { gameState.update(event.tileIndex, any()) } returns updatedGameState
    every { updatedGameState.update(event.playerId, any()) } returns updatedGameState
  }

  @Test
  fun `apply should set the tile's owner to buyer`() {
    every { updatedGameState.tiles[event.tileIndex] } returns tile.copy(owner = buyer.id)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(event.playerId, actual.tiles[event.tileIndex].owner)
  }

  @Test
  fun `apply should detract the tile's price from the buyer's balance`() {
    val buyerWithDetractedFunds = buyer.copy(balance = buyer.balance - tile.price)
    every { updatedGameState.players[event.playerId] } returns buyerWithDetractedFunds
    val actual = eventApplier.apply(event, gameState)
    assertEquals(buyerWithDetractedFunds.balance, actual.players[buyer.id]!!.balance)
  }
}
