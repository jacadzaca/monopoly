package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class BuyTileTest {
  private val buyer = mockk<Player>()
  private val buyersId = UUID.randomUUID()
  private val tile = mockk<Tile>()
  private val tileIndex = Random.nextPositive()
  private val gameState = mockk<GameState>()
  private val purchase = BuyTile(buyer, buyersId, tile, tileIndex, gameState)

    @BeforeEach
    fun setUp() {
      clearAllMocks()
      every { buyer.balance - tile.price } returns Random.nextPositive().toBigInteger()
      every { gameState.updateTile(tileIndex, newOwner = any()) } returns gameState
      every { gameState.updatePlayer(buyersId, newBalance = any()) } returns gameState
    }

    @Test
    fun `execute detracts from buyer's balance`() {
      val newBalance = buyer.balance - tile.price
      purchase.execute()
      verify { gameState.updatePlayer(buyersId, newBalance = newBalance) }
    }

    @Test
    fun `execute changes tile's owner`() {
      purchase.execute()
      verify { gameState.updateTile(tileIndex, newOwner = buyersId) }
    }
}
