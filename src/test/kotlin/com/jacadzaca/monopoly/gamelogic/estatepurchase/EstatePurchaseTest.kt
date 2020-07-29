package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

internal class EstatePurchaseTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val tileIndex = Random.nextInt()
  private val buyersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val create = mockk<(EstateType) -> Estate>()
  private val housePrice = Random.nextInt().toBigInteger()
  private val hotelPrice = Random.nextInt().toBigInteger()
  private val priceOf = mockk<(EstateType) -> BigInteger>()
  private val housePurchase = EstatePurchase(buyer, buyersId, tile, tileIndex, EstateType.HOUSE, create, priceOf)
  private val hotelPurchase = EstatePurchase(buyer, buyersId, tile, tileIndex, EstateType.HOTEL, create, priceOf)

  @BeforeEach
  fun setUp() {
    val house = mockk<Estate>(name = "house")
    val hotel = mockk<Estate>(name = "hotel")
    every { create(EstateType.HOUSE) } returns house
    every { create(EstateType.HOTEL) } returns hotel
    every { priceOf(EstateType.HOUSE) } returns housePrice
    every { priceOf(EstateType.HOTEL) } returns hotelPrice
    every { gameState.update(buyersId, any()) } returns gameState
    every { gameState.update(tileIndex, any()) } returns gameState
    every { tile.addEstate(house) } returns mockk(name = "tile with added house")
    every { tile.addEstate(hotel) } returns mockk(name = "tile with added hotel")
    every { buyer.detractFunds(housePrice) } returns mockk(name = "player after detracting a price of a house")
    every { buyer.detractFunds(hotelPrice) } returns mockk(name = "player after detracting a price of a hotel")
  }

  @Test
  fun `apply adds a estate to the tile`() {
    housePurchase.apply(gameState)
    val tileWithAddedHouse = tile.addEstate(create(EstateType.HOUSE))
    verify { gameState.update(tileIndex, tileWithAddedHouse) }
    hotelPurchase.apply(gameState)
    val tileWithAddedHotel = tile.addEstate(create(EstateType.HOTEL))
    verify { gameState.update(tileIndex, tileWithAddedHotel) }
  }

  @Test
  fun `apply detracts from the buyer's balance`() {
    housePurchase.apply(gameState)
    val playerAfterBuyingHouse = buyer.detractFunds(housePrice)
    verify { gameState.update(buyersId, playerAfterBuyingHouse) }
    hotelPurchase.apply(gameState)
    val playerAfterBuyingHotel = buyer.detractFunds(hotelPrice)
    verify { gameState.update(buyersId, playerAfterBuyingHotel) }
  }
}
