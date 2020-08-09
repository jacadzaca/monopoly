package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.transformations.EstatePurchase
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
  private val actionCreator = mockk<(Player, UUID, Tile, Int, Estate, GameState) -> EstatePurchase>()
  private val house = mockk<Estate.House>(name = "house")
  private val housePurchaseRequest =
    EstatePurchaseRequest(
      buyersId,
      house,
      requiredHousesForHotel,
      actionCreator,
      gameState
    )
  private val hotel = mockk<Estate.Hotel>(name = "hotel")
  private val hotelPurchaseRequest = EstatePurchaseRequest(
      buyersId,
      hotel,
      requiredHousesForHotel,
      actionCreator,
      gameState
    )

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    val buyersPosition = Random.nextInt()
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.players[buyersId] } returns buyer
    every { gameState.tiles[buyersPosition] } returns tile
    every { tile.houseCount() } returns requiredHousesForHotel
    every { buyer.balance } returns Random.nextInt().toBigInteger()
    every { house.price } returns buyer.balance - BigInteger.ONE
    every { hotel.price } returns buyer.balance - BigInteger.ONE
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner, has sufficient funds and wants a house`() {
    every { tile.ownersId } returns buyersId
    val createdEstatePurchase = mockk<EstatePurchase>(name = "estate purchase")
    val buyersPosition = buyer.position
    every { actionCreator(buyer, buyersId, tile, buyersPosition, house, gameState) } returns createdEstatePurchase
    every { house.price } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    val success = ValidationResult.Success(createdEstatePurchase)
    assertEquals(success, housePurchaseRequest.validate())
    assertEquals(success, housePurchaseRequest.validate())
    verify { house.price }
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner, has sufficient funds, the tile has sufficient number of houses and the buyer wants a hotel`() {
    every { tile.ownersId } returns buyersId
    val createdEstatePurchase = mockk<EstatePurchase>(name = "estate purchase")
    val buyersPosition = buyer.position
    every { actionCreator(buyer, buyersId, tile, buyersPosition, hotel, gameState) } returns createdEstatePurchase
    every { hotel.price } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE
    )
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    val success = ValidationResult.Success(createdEstatePurchase)
    assertEquals(success, hotelPurchaseRequest.validate())
    assertEquals(success, hotelPurchaseRequest.validate())
    verify { hotel.price }
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
    every { house.price } returns buyer.balance + BigInteger.ONE
    every { hotel.price } returns buyer.balance + BigInteger.ONE
    assertEquals(failure, housePurchaseRequest.validate())
    assertEquals(failure, hotelPurchaseRequest.validate())
  }

  @Test
  fun `validate returns Failure if the buyer wants a hotel and there are too few houses on tile`() {
    every { tile.houseCount() } returns requiredHousesForHotel - 1
    assertEquals(
      ValidationResult.Failure(EstatePurchaseRequest.notEnoughHouses),
      hotelPurchaseRequest.validate()
    )
  }
}
