package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
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
  private lateinit var buildingFactory: BuildingFactory
  private lateinit var tileManager: TileManagerImpl

  @BeforeEach
  fun setUp() {
    buyer = getTestPlayer()
    buildingFactory = mockk()
    tile = createTile(buyer.id)
    tileManager = TileManagerImpl(buildingFactory, requiredHousesForHotel)
    every { buildingFactory.getPriceFor(any()) } returns BigInteger.ZERO
    every { buildingFactory.create(BuildingType.HOUSE) } returns Building(100.toBigInteger(), BuildingType.HOUSE)
    every { buildingFactory.create(BuildingType.HOTEL) } returns Building(1000.toBigInteger(), BuildingType.HOTEL)
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
    val tileWithProperty = tile.copy(buildings = persistentListOf(buildingFactory.create(BuildingType.HOUSE)))
    assertEquals(tileWithProperty.buildings, tileManager.buyProperty(buyer, tile, BuildingType.HOUSE).buildings)
  }

  @Test
  fun `buyProperty throws an IllegalArgument if the buyer dose not own the tile`() {
    tile = createTile(null)
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, BuildingType.HOUSE)
    }
  }

  @Test
  fun `buyProperty throws IllegalArgument if the buyer dose not own enough houses and wants to buy a hotel`() {
    tile = tileWithNotEnoughHouses()
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, BuildingType.HOTEL)
    }
  }

  private fun tileWithNotEnoughHouses(): Tile {
    val houses = listOf(1 until requiredHousesForHotel).map { buildingFactory.create(BuildingType.HOUSE) }.toPersistentList()
    return tile.copy(buildings = houses)
  }

  @Test
  fun `buyProperty throws IllegalArgument if the buyer has insufficient funds`() {
    every { buildingFactory.getPriceFor(BuildingType.HOUSE) } returns buyer.balance + 123.toBigInteger()
    assertThrows<IllegalArgumentException> {
      tileManager.buyProperty(buyer, tile, BuildingType.HOUSE)
    }
  }
}
