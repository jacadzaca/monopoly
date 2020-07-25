package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.createPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class EstatePurchaseApplierTest {
  private val tile = createTile()
  private val buyer = createPlayer()
  private val gameState = mockk<GameState>()
  private val estateFactory = mockk<EstateFactory>()
  private val eventApplier = EstatePurchaseApplier(estateFactory)
  private val event = VerificationResult.VerifiedEstatePurchaseEvent(buyer, UUID.randomUUID(), tile, 0, EstateType.HOUSE)

  @BeforeEach
  fun setUp() {
    val tileSlot = slot<Tile>()
    val playerSlot = slot<Player>()
    every { estateFactory.getPriceFor(event.estateType) } returns buyer.balance - 10.toBigInteger()
    every { estateFactory.create(event.estateType) } returns createHouse()
    every { gameState.update(event.tileIndex, capture(tileSlot)) } answers {
      every { gameState.tiles[event.tileIndex] } returns tileSlot.captured
      gameState
    }
    every { gameState.update(event.buyerId, capture(playerSlot)) } answers {
      every { gameState.players[event.buyerId] } returns playerSlot.captured
      gameState
    }
  }

  @Test
  fun `apply adds a estate to the tile`() {
    val tileWithEstate = tile.copy(estates = tile.estates.add(estateFactory.create(event.estateType)))
    val actual = eventApplier.apply(event, gameState)
    assertEquals(tileWithEstate.estates, actual.tiles[event.tileIndex].estates)
  }

  @Test
  fun `apply detracts from the buyer's balance`() {
    val playerWithDetractedFunds = buyer.copy(balance = buyer.balance - estateFactory.getPriceFor(event.estateType))
    val actual = eventApplier.apply(event, gameState)
    assertEquals(playerWithDetractedFunds.balance, actual.players[event.buyerId]!!.balance)
  }
}
