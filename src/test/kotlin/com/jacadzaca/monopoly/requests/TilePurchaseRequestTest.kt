package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.TilePurchaseRequest.Companion.TILE_ALREADY_HAS_OWNER
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class TilePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val gameState = mockk<GameState>()
  private val buyersId = UUID.randomUUID()
  private val buyersPosition = Random.nextPositive()
  private val createPurchase = mockk<(Player, UUID, Tile, Int, GameState) -> (BuyTile)>()
  private val request = TilePurchaseRequest(buyersId, gameState, createPurchase)

  @BeforeEach
  fun setUp() {
    every { tile.ownersId } returns null
    every { buyer.position } returns buyersPosition
    every { gameState.tiles[buyersPosition] } returns tile
    every { gameState.players[buyersId] } returns buyer
    every { buyer.balance } returns Random.nextPositive().toBigInteger()
  }

  @Test
  fun `validate returns Success if the buyer has sufficient funds and the tile has no owner`() {
    every { tile.ownersId } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    val purchase = mockk<BuyTile>(name = "purchase")
    every { createPurchase(buyer, buyersId, tile, buyersPosition, gameState) } returns purchase
    val success = ComputationResult.success(purchase)
    assertEquals(success, request.validate())
    assertEquals(success, request.validate())
  }

  @Test
  fun `validate returns Failure if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
    every { tile.ownersId } returnsMany listOf(UUID.randomUUID(), buyersId)
    assertEquals(TILE_ALREADY_HAS_OWNER, request.validate())
    assertEquals(TILE_ALREADY_HAS_OWNER, request.validate())
  }

  @Test
  fun `validate returns Failure if the buyer's balance is less than the tile's price`() {
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, request.validate())
  }

  @Test
  fun `validate returns Failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(INVALID_PLAYER_ID, request.validate())
  }
}
