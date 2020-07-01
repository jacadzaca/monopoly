package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.createHotel
import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import kotlinx.collections.immutable.toPersistentList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class TileDeltaCalculatorTest {
  private val deltaCalculator = TileDeltaCalculator()
  private val previous = createTile()

  @Test
  fun `calculate includes change in buildings `() {
    val change = listOf(createHouse(), createHotel())
    val current = previous.copy(buildings = change.toPersistentList())

    val expected = TileDelta(changeInBuildings = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInBuildings, actual.changeInBuildings)
  }

  @Test
  fun `calculate includes change in price`() {
    val change = 10.toBigInteger()
    val current = previous.copy(price = previous.price + change)

    val expected = TileDelta(changeInPrice = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInPrice, actual.changeInPrice)
  }

  @Test
  fun `calculate sets changeInOwner to current's owner if owners differ`() {
    val change = UUID.randomUUID()
    val current = previous.copy(owner = change)

    val expected = TileDelta(changeInOwner = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInOwner, actual.changeInOwner)
  }

  @Test
  fun `calculate sets changeInOwner to current if previously no owner`() {
    val change = UUID.randomUUID()
    val previous = createTile(null)
    val current = previous.copy(owner = change)

    val expected = TileDelta(changeInOwner = change)
    val actual = deltaCalculator.calculate(current, previous)

    assertEquals(expected.changeInOwner, actual.changeInOwner)
  }
}
