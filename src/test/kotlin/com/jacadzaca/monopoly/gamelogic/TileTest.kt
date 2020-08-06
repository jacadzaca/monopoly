package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import io.mockk.spyk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TileTest {
  private val houses = spyk(persistentListOf<Estate>())
  private val hotels = spyk(persistentListOf<Estate>())
  private val tile = Tile(houses, hotels, Random.nextInt().toBigInteger(), null)

  @Test
  fun `totalRent consists of rent for hotels and houses`() {
    val house = create(EstateType.HOUSE)
    val hotel = create(EstateType.HOTEL)
    val tile = tile.copy(houses = houses.add(house), hotels = hotels.add(hotel))
    assertEquals(house.rent + hotel.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all houses`() {
    val house = create(EstateType.HOUSE)
    val house1 = create(EstateType.HOUSE)
    val tile = tile.copy(houses = houses.addAll(listOf(house, house1)))
    assertEquals(house.rent + house1.rent, tile.totalRent())
  }

  @Test
  fun `totalRent sums up rent from all hotels`() {
    val hotel = create(EstateType.HOTEL)
    val hotel1 = create(EstateType.HOTEL)
    val tile = tile.copy(hotels = hotels.addAll(listOf(hotel, hotel1)))
    assertEquals(hotel.rent + hotel1.rent, tile.totalRent())
  }

  @Test
  fun `addEstate adds the estate to houses if it's a house`() {
    val house = create(EstateType.HOUSE)
    assertTrue(tile.addEstate(house).houses.contains(house))
    verify { houses.add(house) }
  }

  @Test
  fun `addEstate adds the estate to hotels it's a hotel`() {
    val hotel = create(EstateType.HOTEL)
    assertTrue(tile.addEstate(hotel).hotels.contains(hotel))
    verify { hotels.add(hotel) }
  }

  private fun create(type: EstateType) = Estate(Random.nextInt().toBigInteger(), type)
}
