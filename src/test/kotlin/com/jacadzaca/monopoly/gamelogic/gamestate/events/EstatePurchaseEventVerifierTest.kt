package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.EstatePurchaseEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.EstatePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.VerifiedEstatePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class EstatePurchaseEventVerifierTest {
  private val tile = mockk<Tile>()
  private val buyer = getTestPlayer()
  private val requiredHousesForHotel = 2
  private val gameState = mockk<GameState>()
  private val estateFactory = mockk<EstateFactory>()
  private val buyHotelEvent =
    EstatePurchaseEvent(
      buyer.id,
      EstateType.HOTEL,
      tileIndex = 21
    )
  private val verifiedBuyHotelEvent = VerifiedEstatePurchaseEvent(buyHotelEvent)
  private val buyHouseEvent =
    EstatePurchaseEvent(
      buyer.id,
      EstateType.HOUSE,
      tileIndex = 32
    )
  private val verifiedBuyHouseEvent = VerifiedEstatePurchaseEvent(buyHouseEvent)
  private val eventVerifier =
    EstatePurchaseEventVerifier(
      estateFactory,
      requiredHousesForHotel
    )

  @BeforeEach
  fun setUp() {
    every { gameState.players.getValue(buyer.id) } returns buyer
    every { gameState.tiles[buyHouseEvent.tileIndex] } returns tile
    every { gameState.tiles[buyHotelEvent.tileIndex] } returns tile
    every { tile.owner } returns null
    every { tile.houseCount() } returns requiredHousesForHotel
  }

  @Test
  fun `verify returns the inputted event if the buyer is the tile's owner, the buyer has sufficient funds and the buyer wants a house`() {
    every { tile.owner } returns buyer.id
    every { estateFactory.getPriceFor(EstateType.HOUSE) } returns buyer.balance - 10.toBigInteger()
    assertEquals(verifiedBuyHouseEvent, eventVerifier.verify(buyHouseEvent, gameState))
  }

  @Test
  fun `verify returns the inputted event if the tile's owner is the buyer, the buyer has sufficient funds, there is sufficient number of houses and the buyer wants a hotel`() {
    every { tile.owner } returns buyer.id
    every { estateFactory.getPriceFor(EstateType.HOTEL) } returns buyer.balance - 10.toBigInteger()
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    assertEquals(verifiedBuyHotelEvent, eventVerifier.verify(buyHotelEvent, gameState))
    assertEquals(verifiedBuyHotelEvent, eventVerifier.verify(buyHotelEvent, gameState))
  }

  @Test
  fun `verify returns null if the buyer dose not own the tile`() {
    val otherOwner = UUID.randomUUID()
    every { tile.owner } returnsMany listOf(otherOwner, null)
    assertNull(eventVerifier.verify(buyHouseEvent, gameState))
    assertNull(eventVerifier.verify(buyHouseEvent, gameState))
  }

  @Test
  fun `verify returns null if the buyer has insufficient funds`() {
    every { estateFactory.getPriceFor(any()) } returns buyer.balance + 10.toBigInteger()
    assertNull(eventVerifier.verify(buyHouseEvent, gameState))
    assertNull(eventVerifier.verify(buyHotelEvent, gameState))
  }

  @Test
  fun `verify returns null if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returns requiredHousesForHotel - 1
    assertNull(eventVerifier.verify(buyHotelEvent, gameState))
  }
}
