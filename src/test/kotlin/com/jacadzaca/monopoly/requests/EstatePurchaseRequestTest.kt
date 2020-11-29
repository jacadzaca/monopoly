package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.EstatePurchaseRequest.Companion.NOT_ENOUGH_HOUSES
import com.jacadzaca.monopoly.requests.EstatePurchaseRequest.Companion.TILE_NOT_OWNED_BY_BUYER
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
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
  private val requiredHousesForHotel = Random.nextPositive()
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
    val buyersPosition = Random.nextPositive()
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.players[buyersId] } returns buyer
    every { gameState.tiles[buyersPosition] } returns tile
    every { tile.houseCount() } returns requiredHousesForHotel
    every { buyer.balance } returns Random.nextPositive().toBigInteger()
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
    val success = Computation.success(createdEstatePurchase)
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
    val success = Computation.success(createdEstatePurchase)
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
    assertEquals(TILE_NOT_OWNED_BY_BUYER, housePurchaseRequest.validate())
    assertEquals(TILE_NOT_OWNED_BY_BUYER, hotelPurchaseRequest.validate())
  }

  @Test
  fun `validate returns Failure if the buyer has insufficient funds`() {
    every { house.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + Random.nextPositive().toBigInteger()
    )
    every { hotel.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + Random.nextPositive().toBigInteger()
    )
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, housePurchaseRequest.validate())
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, housePurchaseRequest.validate())
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, hotelPurchaseRequest.validate())
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, hotelPurchaseRequest.validate())
  }

  @Test
  fun `validate returns Failure if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returnsMany listOf(
      requiredHousesForHotel - 1,
      Random.nextPositive(until = requiredHousesForHotel)
    )
    assertEquals(NOT_ENOUGH_HOUSES, hotelPurchaseRequest.validate())
    assertEquals(NOT_ENOUGH_HOUSES, hotelPurchaseRequest.validate())
  }
}
