package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.HotelPurchaseValidator.Companion.NOT_ENOUGH_HOUSES
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class HotelPurchaseValidatorTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val buyersId = UUID.randomUUID()
  private val requiredHousesForHotel = Random.nextPositive()
  private val gameState = mockk<GameState>()
  private val createPurchase = mockk<(Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate>()
  private val hotel = mockk<Estate.Hotel>(name = "hotel")
  private val hotelPurchaseValidator = HotelPurchaseValidator(createPurchase, requiredHousesForHotel, hotel)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    val buyersPosition = Random.nextPositive()
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.players[buyersId] } returns buyer
    every { gameState.tiles[buyersPosition] } returns tile
    every { buyer.balance } returns Random.nextPositive().toBigInteger()
    every { hotel.price } returns buyer.balance - BigInteger.ONE
  }

  @Test
  fun `validate returns a success if the buyer is the tile's owner, has sufficient funds and the tile has sufficient number of houses`() {
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
    assertEquals(success, hotelPurchaseValidator.validate(buyersId, gameState))
    assertEquals(success, hotelPurchaseValidator.validate(buyersId, gameState))
  }

  @Test
  fun `validate returns a failure if the buyer dose not own the tile`() {
    val otherOwner = UUID.randomUUID()
    every { tile.ownersId } returnsMany listOf(otherOwner, null)
    assertEquals(TILE_NOT_OWNED_BY_BUYER, hotelPurchaseValidator.validate(buyersId, gameState))
  }

  @Test
  fun `validate returns a failure if the buyer has insufficient funds`() {
    every { hotel.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + Random.nextPositive().toBigInteger()
    )
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, hotelPurchaseValidator.validate(buyersId, gameState))
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, hotelPurchaseValidator.validate(buyersId, gameState))
  }

  @Test
  fun `validate returns a failure if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returnsMany listOf(
      requiredHousesForHotel - 1,
      Random.nextPositive(until = requiredHousesForHotel)
    )
    assertEquals(NOT_ENOUGH_HOUSES, hotelPurchaseValidator.validate(buyersId, gameState))
    assertEquals(NOT_ENOUGH_HOUSES, hotelPurchaseValidator.validate(buyersId, gameState))
  }

  @Test
  fun `validate returns a failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(INVALID_PLAYER_ID, hotelPurchaseValidator.validate(buyersId, gameState))
  }
}
