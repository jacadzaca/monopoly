package com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
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
  private val verifiedBuyHotelEvent =
    VerificationResult.VerifiedEstatePurchaseEvent(
      buyer,
      buyer.id,
      tile,
      21,
      EstateType.HOTEL
    )
  private val buyHouseEvent =
    EstatePurchaseEvent(
      buyer.id,
      EstateType.HOUSE,
      tileIndex = 32
    )
  private val verifiedBuyHouseEvent =
    VerificationResult.VerifiedEstatePurchaseEvent(
      buyer,
      buyer.id,
      tile,
      32,
      EstateType.HOUSE
    )
  private val tileExists = mockk<(Int, GameState) -> Boolean>()
  private val eventVerifier =
    EstatePurchaseEventVerifier(
      estateFactory,
      requiredHousesForHotel,
      tileExists
    )

  @BeforeEach
  fun setUp() {
    every { tile.owner } returns buyer.id
    every { gameState.players[buyer.id] } returns buyer
    every { tile.houseCount() } returns requiredHousesForHotel
    every { gameState.tiles[buyHouseEvent.tileIndex] } returns tile
    every { gameState.tiles[buyHotelEvent.tileIndex] } returns tile
    every { tileExists(buyHouseEvent.tileIndex, gameState) } returns true
    every { tileExists(buyHotelEvent.tileIndex, gameState) } returns true
    every { estateFactory.getPriceFor(any()) } returns buyer.balance - BigInteger.ONE
  }

  @Test
  fun `verify returns the inputted event if the buyer is the tile's owner, the buyer has sufficient funds and the buyer wants a house`() {
    every { tile.owner } returns buyer.id
    every { estateFactory.getPriceFor(EstateType.HOUSE) } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    assertEquals(verifiedBuyHouseEvent, eventVerifier.verify(buyHouseEvent, gameState))
    assertEquals(verifiedBuyHouseEvent, eventVerifier.verify(buyHouseEvent, gameState))
  }

  @Test
  fun `verify returns the inputted event if the tile's owner is the buyer, the buyer has sufficient funds, there is sufficient number of houses and the buyer wants a hotel`() {
    every { tile.owner } returns buyer.id
    every { estateFactory.getPriceFor(EstateType.HOTEL) } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    assertEquals(verifiedBuyHotelEvent, eventVerifier.verify(buyHotelEvent, gameState))
    assertEquals(verifiedBuyHotelEvent, eventVerifier.verify(buyHotelEvent, gameState))
  }

  @Test
  fun `verify returns Failure if the buyer dose not own the tile`() {
    val otherOwner = UUID.randomUUID()
    every { tile.owner } returnsMany listOf(otherOwner, null)
    val failure = VerificationResult.Failure(
      EstatePurchaseEventVerifier.tileNotOwnedByBuyer
    )
    assertEquals(failure, eventVerifier.verify(buyHouseEvent, gameState))
    assertEquals(failure, eventVerifier.verify(buyHouseEvent, gameState))
  }

  @Test
  fun `verify returns Failure if the buyer has insufficient funds`() {
    every { estateFactory.getPriceFor(any()) } returns buyer.balance + BigInteger.ONE
    val failure =
      VerificationResult.Failure(GameEventVerifier.buyerHasInsufficientBalance)
    assertEquals(failure, eventVerifier.verify(buyHouseEvent, gameState))
    assertEquals(failure, eventVerifier.verify(buyHotelEvent, gameState))
  }

  @Test
  fun `verify returns Failure if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returns requiredHousesForHotel - 1
    assertEquals(
      VerificationResult.Failure(
        EstatePurchaseEventVerifier.notEnoughHouses
      ),
      eventVerifier.verify(buyHotelEvent, gameState)
    )
  }

  @Test
  fun `verify returns Failure if event references a player non-existing player`() {
    val failure =
      VerificationResult.Failure(GameEventVerifier.invalidPlayerId)
    every { gameState.players[buyHouseEvent.buyer] } returns null
    assertEquals(failure, eventVerifier.verify(buyHouseEvent, gameState))
    every { gameState.players[buyHotelEvent.buyer] } returns null
    assertEquals(failure, eventVerifier.verify(buyHotelEvent, gameState))
  }

  @Test
  fun `verify returns Failure if event references a tile that is not on the board`() {
    val failure =
      VerificationResult.Failure(GameEventVerifier.invalidTileIndex)
    every { tileExists(buyHouseEvent.tileIndex, gameState) } returns false
    assertEquals(failure, eventVerifier.verify(buyHouseEvent, gameState))
    every { tileExists(buyHotelEvent.tileIndex, gameState) } returns false
    assertEquals(failure, eventVerifier.verify(buyHotelEvent, gameState))
  }
}
