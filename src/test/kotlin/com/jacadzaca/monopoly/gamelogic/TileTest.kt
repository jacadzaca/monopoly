package com.jacadzaca.monopoly.gamelogic

import io.mockk.*
import java.math.BigInteger
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import com.jacadzaca.monopoly.nextPositive
import kotlin.random.Random

internal class TileTest {
  private val houses = spyk(persistentListOf<Estate>())
  private val hotels = spyk(persistentListOf<Estate>())
  private val house = mockk<Estate.House>(name = "house")
  private val hotel = mockk<Estate.Hotel>(name = "hotel")
  private val baseRent = Random.nextPositive().toBigInteger()
  private val tile = Tile(houses, hotels, Random.nextPositive().toBigInteger(), null)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { house.rent } returns Random.nextPositive().toBigInteger()
    every { hotel.rent } returns Random.nextPositive().toBigInteger()
  }

  @Test
  fun `totalRent consists of rent for hotels and houses and baseRent`() {
    val tile = tile.copy(houses = houses.add(house), hotels = hotels.add(hotel), baseRent = baseRent)
    assertEquals(baseRent + house.rent + hotel.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all houses`() {
    val tile = tile.copy(houses = houses.addAll(listOf(house, house)), baseRent = BigInteger.ZERO)
    assertEquals(house.rent + house.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all hotels`() {
    val tile = tile.copy(hotels = hotels.addAll(listOf(hotel, hotel)), baseRent = BigInteger.ZERO)
    assertEquals(hotel.rent + hotel.rent, tile.totalRent())
  }

  @Test
  fun `addEstate adds the estate to houses if it's a house`() {
    assertTrue(tile.addEstate(house).houses.contains(house))
    verify { houses.add(house) }
  }

  @Test
  fun `addEstate adds the estate to hotels it's a hotel`() {
    assertTrue(tile.addEstate(hotel).hotels.contains(hotel))
    verify { hotels.add(hotel) }
  }
}

