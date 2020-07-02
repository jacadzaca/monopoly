package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.createHotel
import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import kotlinx.collections.immutable.toPersistentList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class TileDeltaCalculatorTest {
  private val deltaCalculator = TileDeltaCalculator()
  private val previous = createTile()

  @Test
  fun `calculate sets the changeInBuildings to current estates if they change`() {
    val change = listOf(createHouse(), createHotel())
    val current = previous.copy(buildings = change.toPersistentList())

    val expected = TileDelta(changeInBuildings = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInBuildings, actual.changeInBuildings)
  }

  @Test
  fun `calculate sets changeInBuildings to null if no change`() {
    assertEquals(null, deltaCalculator.calculate(previous, previous).changeInBuildings)
  }

  @Test
  fun `calculate sets the changeInPrice to the difference between current and previous price`() {
    val change = 10.toBigInteger()
    val current = previous.copy(price = previous.price + change)

    val expected = TileDelta(changeInPrice = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInPrice, actual.changeInPrice)
  }

  @Test
  fun `calculate sets changeInPrice to null if no changes`() {
    assertEquals(null, deltaCalculator.calculate(previous, previous).changeInPrice)
  }

  @Test
  fun `calculate sets changeInOwner to current owner if owners differ`() {
    val change = UUID.randomUUID()
    val current = previous.copy(owner = change)

    val expected = TileDelta(changeInOwner = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInOwner, actual.changeInOwner)
  }

  @Test
  fun `calculate sets changeInOwner to null if no change`() {
    assertEquals(null, deltaCalculator.calculate(previous, previous).changeInPrice)
  }
}
