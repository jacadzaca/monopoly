package com.jacadzaca.monopoly.gamelogic

import io.mockk.*
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TileTest {
  private val houses = spyk(persistentListOf<Estate>())
  private val hotels = spyk(persistentListOf<Estate>())
  private val house = mockk<Estate.House>(name = "house")
  private val hotel = mockk<Estate.Hotel>(name = "hotel")
  private val tile = Tile(houses, hotels, Random.nextInt().toBigInteger(), null)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { house.rent } returns Random.nextInt().toBigInteger()
    every { hotel.rent } returns Random.nextInt().toBigInteger()
  }

  @Test
  fun `totalRent consists of rent for hotels and houses`() {
    val tile = tile.copy(houses = houses.add(house), hotels = hotels.add(hotel))
    assertEquals(house.rent + hotel.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all houses`() {
    val tile = tile.copy(houses = houses.addAll(listOf(house, house)))
    assertEquals(house.rent + house.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all hotels`() {
    val tile = tile.copy(hotels = hotels.addAll(listOf(hotel, hotel)))
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
