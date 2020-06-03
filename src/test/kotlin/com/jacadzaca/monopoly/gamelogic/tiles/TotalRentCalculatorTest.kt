package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.createHotel
import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TotalRentCalculatorTest {
  @Test
  fun `getTotalRentFor should sum up the rents of individual buildings`() {
    val rent = 123.toBigInteger()
    val rent1 = 234.toBigInteger()
    val tile = createTile().copy(buildings = persistentListOf(createHouse(rent), createHotel(rent1)))
    assertEquals(rent + rent1,  tile.totalRent())
  }
}
