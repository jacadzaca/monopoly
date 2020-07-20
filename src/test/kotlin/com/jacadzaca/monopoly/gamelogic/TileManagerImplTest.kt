package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.gamelogic.tiles.TileManagerImpl
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger

internal class TileManagerImplTest {
  private lateinit var tile: Tile
  private lateinit var buyer: Player
  private val requiredHousesForHotel = 4
  private lateinit var estateFactory: EstateFactory
  private lateinit var tileManager: TileManagerImpl

  @BeforeEach
  fun setUp() {
    buyer = getTestPlayer()
    estateFactory = mockk()
    tile = createTile(buyer.id)
    tileManager = TileManagerImpl(estateFactory, requiredHousesForHotel)
    every { estateFactory.getPriceFor(any()) } returns BigInteger.ZERO
    every { estateFactory.create(EstateType.HOUSE) } returns Estate(100.toBigInteger(), EstateType.HOUSE)
    every { estateFactory.create(EstateType.HOTEL) } returns Estate(1000.toBigInteger(), EstateType.HOTEL)
  }

  @Test
  fun `buyTile should change the tile's owner`() {
    val tileThatCanBeBought = createTile(null).copy(price = buyer.balance - 123.toBigInteger())
    val boughtTile = tileThatCanBeBought.copy(owner = buyer.id)
    assertEquals(boughtTile.owner, tileManager.buyTile(buyer, tileThatCanBeBought).owner)
  }

  @Test
  fun `buyTile throws IllegalArgument if buyer has insufficient funds`() {
    val tooExpensiveTile = createTile().copy(price = buyer.balance + 123.toBigInteger())
    assertThrows<IllegalArgumentException> {
      tileManager.buyTile(buyer, tooExpensiveTile)
    }
  }

  @Test
  fun `buyTile throws IllegalArgument if buyer already own the tile`() {
    val tileAlreadyOwnedByBuyer = createTile(buyer.id)
    assertThrows<IllegalArgumentException> {
      tileManager.buyTile(buyer, tileAlreadyOwnedByBuyer)
    }
  }

  @Test
  fun `buyProperty should add the property to the tile's listing`() {
    val tileWithProperty = tile.copy(estates = persistentListOf(estateFactory.create(EstateType.HOUSE)))
    assertEquals(tileWithProperty.estates, tileManager.buyProperty(buyer, tile, EstateType.HOUSE).estates)
  }

  @Test
  fun `buyProperty throws an IllegalArgument if the buyer dose not own the tile`() {
    tile = createTile(null)
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, EstateType.HOUSE)
    }
  }

  @Test
  fun `buyProperty throws IllegalArgument if the buyer dose not own enough houses and wants to buy a hotel`() {
    tile = tileWithNotEnoughHouses()
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, EstateType.HOTEL)
    }
  }

  private fun tileWithNotEnoughHouses(): Tile {
    val houses = listOf(1 until requiredHousesForHotel).map { estateFactory.create(EstateType.HOUSE) }.toPersistentList()
    return tile.copy(estates = houses)
  }

  @Test
  fun `buyProperty throws IllegalArgument if the buyer has insufficient funds`() {
    every { estateFactory.getPriceFor(EstateType.HOUSE) } returns buyer.balance + 123.toBigInteger()
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, EstateType.HOUSE)
    }
  }
}
