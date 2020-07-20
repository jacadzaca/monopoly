package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.createHotel
import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TileTest {
  private lateinit var tile: Tile

  @BeforeEach
  fun setUp() {
    tile = createTile()
  }

  @Test
  fun `getTotalRentFor should sum up the rents of individual buildings`() {
    val rent = 123.toBigInteger()
    val rent1 = 234.toBigInteger()
    tile = tile.copy(estates = persistentListOf(createHouse(rent), createHotel(rent1)))
    assertEquals(rent + rent1,  tile.totalRent())
  }

  @Test
  fun `houseCount should count the number of houses on a tile`() {
    tile = tile.copy(estates = persistentListOf(createHouse(), createHouse(), createHotel()))
    assertEquals(2, tile.houseCount())
  }
}
