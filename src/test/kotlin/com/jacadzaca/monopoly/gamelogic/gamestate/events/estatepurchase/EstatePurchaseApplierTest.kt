package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
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

internal class EstatePurchaseApplierTest {
  private val tile = createTile()
  private val buyer = getTestPlayer()
  private val tileSlot = slot<Tile>()
  private val playerSlot = slot<Player>()
  private val gameState = mockk<GameState>()
  private val estateFactory = mockk<EstateFactory>()
  private val updatedGameState =  mockk<GameState>()
  private val eventApplier = EstatePurchaseApplier(estateFactory)
  private val event = VerificationResult.VerifiedEstatePurchaseEvent(buyer, buyer.id, tile, 0, EstateType.HOUSE)

  @BeforeEach
  fun setUp() {
    every { estateFactory.getPriceFor(event.estateType) } returns buyer.balance - 10.toBigInteger()
    every { estateFactory.create(event.estateType) } returns createHouse()
    // WARNING! this makes the order of updates a tested condition - first update the tile then the player
    every { gameState.update(event.tileIndex, capture(tileSlot)) } returns updatedGameState
    every { updatedGameState.update(event.playerId, capture(playerSlot)) } returns updatedGameState
  }

  @Test
  fun `apply should add a estate to the tile`() {
    val tileWithEstate = tile.copy(estates = tile.estates.add(estateFactory.create(event.estateType)))
    val actual = eventApplier.apply(event, gameState)
    every { updatedGameState.tiles[event.tileIndex] } returns tileSlot.captured
    assertEquals(tileWithEstate.estates, actual.tiles[event.tileIndex].estates)
  }

  @Test
  fun `apply should detract from the buyer's balance`() {
    val playerWithDetractedFunds = buyer.copy(balance = buyer.balance - estateFactory.getPriceFor(event.estateType))
    val actual = eventApplier.apply(event, gameState)
    every { updatedGameState.players[event.playerId] } returns playerSlot.captured
    assertEquals(playerWithDetractedFunds.balance, actual.players[buyer.id]!!.balance)
  }
}
