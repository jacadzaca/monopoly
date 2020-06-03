package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.getTestPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TileManagerImplTest {
  private val tileManager: TileManagerImpl = TileManagerImpl()
  private lateinit var buyer: Player

  @BeforeEach
  fun setUp() {
    buyer = getTestPlayer()
  }

  @Test
  fun `buyTile should change the tile's owner`() {
    val tileThatCanBeBought = createTile(null).copy(price = buyer.balance - 123.toBigInteger())
    val boughtTile = buyer.position.copy(owner = buyer.id)
    assertEquals(boughtTile, tileManager.buyTile(buyer, tileThatCanBeBought))
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
}
