package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.Request.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.Request.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.Request.Companion.TILE_NOT_OWNED_BY_BUYER
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class HousePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val buyersId = UUID.randomUUID()
  private val buyersPosition = Random.nextPositive()
  private val gameState = mockk<GameState>()
  private val createPurchase = mockk<(Player, UUID, Tile, Int, Estate, GameState) -> BuyEstate>()
  private val house = mockk<Estate.House>(name = "house")
  private val request = HousePurchaseRequest(buyersId, house, createPurchase)


  @BeforeEach
  fun setUp() {
    every { tile.ownersId } returns buyersId
    every { buyer.position } returns buyersPosition
    every { gameState.tiles[buyersPosition] } returns tile
    every { buyer.balance } returns Random.nextPositive().toBigInteger()
    every { house.price } returns buyer.balance - BigInteger.ONE
    every { gameState.players[buyersId] } returns buyer
  }

  @Test
  fun `validate returns Success if the buyer is the tile's owner and has sufficient funds`() {
    val createdEstatePurchase = mockk<BuyEstate>()
    every { createPurchase(buyer, buyersId, tile, buyersPosition, house, gameState) } returns createdEstatePurchase
    every { tile.ownersId } returns buyersId
    every { house.price } returnsMany listOf(
      buyer.balance,
      buyer.balance - BigInteger.ONE,
      buyer.balance.toInt().toBigInteger()
    )
    val success = Computation.success(createdEstatePurchase)
    assertEquals(success, request.validate(gameState))
    assertEquals(success, request.validate(gameState))
    assertEquals(success, request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer dose not own the tile`() {
    every { tile.ownersId } returnsMany listOf(
      null,
      UUID.randomUUID()
    )
    assertEquals(TILE_NOT_OWNED_BY_BUYER, request.validate(gameState))
    assertEquals(TILE_NOT_OWNED_BY_BUYER, request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer has insufficient funds`() {
    every { house.price } returnsMany listOf(
      buyer.balance + BigInteger.ONE,
      buyer.balance + Random.nextPositive().toBigInteger()
    )
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, request.validate(gameState))
    assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(INVALID_PLAYER_ID, request.validate(gameState))
  }
}