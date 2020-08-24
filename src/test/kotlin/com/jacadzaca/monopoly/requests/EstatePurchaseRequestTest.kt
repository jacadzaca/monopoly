package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.EstatePurchaseRequest.Companion.notEnoughHouses
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class EstatePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val buyersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val requiredHousesForHotel = randomPositive()
  private val createPurchase = mockk<(Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate>()
  private val house = mockk<Estate.House>(name = "house")
  private val housePurchaseRequest =
    EstatePurchaseRequest(buyersId, house, requiredHousesForHotel, createPurchase, gameState)
  private val hotel = mockk<Estate.Hotel>(name = "hotel")
  private val hotelPurchaseRequest =
    EstatePurchaseRequest(buyersId, hotel, requiredHousesForHotel, createPurchase, gameState)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    val buyersPosition = randomPositive()
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.players[buyersId] } returns buyer
    every { gameState.tiles[buyersPosition] } returns tile
    every { tile.houseCount() } returns requiredHousesForHotel
    every { buyer.balance } returns randomPositive().toBigInteger()
    every { house.price } returns buyer.balance - BigInteger.ONE
    every { hotel.price } returns buyer.balance - BigInteger.ONE
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner, has sufficient funds and wants a house`() {
    every { tile.ownersId } returns buyersId
    val createdEstatePurchase = mockk<BuyEstate>()
    val buyersPosition = buyer.position
    every { createPurchase(buyer, buyersId, tile, buyersPosition, house, gameState) } returns createdEstatePurchase
    every { house.price } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE,
      Random.nextInt(1, buyer.balance.toInt()).toBigInteger()
    )
    val success = ValidationResult.Success(createdEstatePurchase)
    assertEquals(success, housePurchaseRequest.validate())
    assertEquals(success, housePurchaseRequest.validate())
    verify { house.price }
    verify { tile.ownersId }
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner, has sufficient funds, the tile has sufficient number of houses and the buyer wants a hotel`() {
    every { tile.ownersId } returns buyersId
    val createdEstatePurchase = mockk<BuyEstate>()
    val buyersPosition = buyer.position
    every { createPurchase(buyer, buyersId, tile, buyersPosition, hotel, gameState) } returns createdEstatePurchase
    every { hotel.price } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE,
      buyer.balance - Random.nextInt(1, buyer.balance.toInt()).toBigInteger()
    )
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    val success = ValidationResult.Success(createdEstatePurchase)
    assertEquals(success, hotelPurchaseRequest.validate())
    assertEquals(success, hotelPurchaseRequest.validate())
    verify { hotel.price }
    verify { tile.ownersId }
    verify { tile.houseCount() }
  }

  @Test
  fun `validate returns Failure if the buyer dose not own the tile`() {
    val otherOwner = UUID.randomUUID()
    every { tile.ownersId } returnsMany listOf(otherOwner, null)
    val failure = ValidationResult.Failure(EstatePurchaseRequest.tileNotOwnedByBuyer)
    assertEquals(failure, housePurchaseRequest.validate())
    assertEquals(failure, hotelPurchaseRequest.validate())
  }

  @Test
  fun `validate returns Failure if the buyer has insufficient funds`() {
    val failure = ValidationResult.Failure(Request.buyerHasInsufficientBalance)
    every { house.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + randomPositive().toBigInteger()
    )
    every { hotel.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + randomPositive().toBigInteger()
    )
    assertEquals(failure, housePurchaseRequest.validate())
    assertEquals(failure, housePurchaseRequest.validate())
    assertEquals(failure, hotelPurchaseRequest.validate())
    assertEquals(failure, hotelPurchaseRequest.validate())
  }

  @Test
  fun `validate returns Failure if the buyer wants a hotel and there are too few houses on tile`() {
    val failure = ValidationResult.Failure(notEnoughHouses)
    every { tile.houseCount() } returnsMany listOf(
      requiredHousesForHotel - 1,
      requiredHousesForHotel - randomPositive()
    )
    assertEquals(failure, hotelPurchaseRequest.validate())
    assertEquals(failure, hotelPurchaseRequest.validate())
  }
}
