package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

internal class EstatePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val buyersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val requiredHousesForHotel = Random.nextInt()
  private val priceOf = mockk<(EstateType) -> BigInteger>()
  private val housePurchaseRequest = EstatePurchaseRequest(buyersId, EstateType.HOUSE, priceOf, requiredHousesForHotel)
  private val hotelPurchaseRequest = EstatePurchaseRequest(buyersId, EstateType.HOTEL, priceOf, requiredHousesForHotel)

  @BeforeEach
  fun setUp() {
    val buyersPosition = Random.nextInt()
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.players[buyersId] } returns buyer
    every { gameState.tiles[buyersPosition] } returns tile
    every { tile.houseCount() } returns requiredHousesForHotel
    every { buyer.balance } returns Random.nextInt().toBigInteger()
    every { priceOf(any()) } returns buyer.balance - BigInteger.ONE
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner, has sufficient funds and wants a house`() {
    clearMocks(priceOf)
    every { tile.ownersId } returns buyersId
    every { priceOf(EstateType.HOUSE) } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    val success =
      ValidationResult.Success(EstatePurchase.create(buyer, buyersId, tile, buyer.position, EstateType.HOUSE))
    assertEquals(success, housePurchaseRequest.validate(gameState))
    assertEquals(success, housePurchaseRequest.validate(gameState))
  }

  @Test
  fun `validate returns the inputted event if the buyer is the tile's owner, has sufficient funds, the tile has sufficient number of houses and the buyer wants a hotel`() {
    clearMocks(priceOf)
    every { tile.ownersId } returns buyersId
    every { priceOf(EstateType.HOTEL) } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    val success =
      ValidationResult.Success(EstatePurchase.create(buyer, buyersId, tile, buyer.position, EstateType.HOTEL))
    assertEquals(success, hotelPurchaseRequest.validate(gameState))
    assertEquals(success, hotelPurchaseRequest.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer dose not own the tile`() {
    val otherOwner = UUID.randomUUID()
    every { tile.ownersId } returnsMany listOf(otherOwner, null)
    val failure = ValidationResult.Failure(EstatePurchaseRequest.tileNotOwnedByBuyer)
    assertEquals(failure, housePurchaseRequest.validate(gameState))
    assertEquals(failure, hotelPurchaseRequest.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer has insufficient funds`() {
    val failure = ValidationResult.Failure(Request.buyerHasInsufficientBalance)
    every { priceOf(EstateType.HOUSE) } returns buyer.balance + BigInteger.ONE
    assertEquals(failure, housePurchaseRequest.validate(gameState))
    clearMocks(priceOf)
    every { priceOf(EstateType.HOTEL) } returns buyer.balance + BigInteger.ONE
    assertEquals(failure, hotelPurchaseRequest.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returns requiredHousesForHotel - 1
    assertEquals(
      ValidationResult.Failure(EstatePurchaseRequest.notEnoughHouses),
      hotelPurchaseRequest.validate(gameState)
    )
  }
}
