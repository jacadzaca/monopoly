package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.tiles.RentCalculatorImpl
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RentCalculatorImplTest {
    private lateinit var rentCalculatorImpl: RentCalculatorImpl

    @BeforeEach
    fun setUp() {
      rentCalculatorImpl = RentCalculatorImpl()
    }

    @Test
    fun `getTotalRentFor should sum up the rents of individual buildings`() {
      val rent = 123.toBigInteger()
      val rent1 = 234.toBigInteger()
      val tile = createTile().copy(buildings = listOf(Building(rent), Building(rent1)))
      assertEquals(rent + rent1,  rentCalculatorImpl.getTotalRentFor(tile))
    }
}
