package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.transformations.*
import com.jacadzaca.monopoly.requests.Request.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.requests.Request.Companion.invalidPlayerId
import com.jacadzaca.monopoly.requests.TilePurchaseRequest.Companion.tileAlreadyHasOwner
import io.mockk.*
import java.math.BigInteger
import java.util.UUID
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

internal class TilePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val gameState = mockk<GameState>()
  private val buyersId = UUID.randomUUID()
  private val buyersPosition = randomPositive()
  private val createPurchase = mockk<(Player, UUID, Tile, Int, GameState) -> (TilePurchase)>()
  private val request = TilePurchaseRequest(buyersId, gameState, createPurchase)

  @BeforeEach
  fun setUp() {
    every { tile.ownersId } returns null
    every { buyer.position } returns buyersPosition
    every { gameState.tiles[buyersPosition] } returns tile
    every { gameState.players[buyersId] } returns buyer
    every { buyer.balance } returns randomPositive().toBigInteger()
  }

  @Test
  fun `validate returns Success if the buyer has sufficient funds and the tile has no owner`() {
    every { tile.ownersId } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    val purchase = mockk<TilePurchase>(name = "purchase")
    every { createPurchase(buyer, buyersId, tile, buyersPosition, gameState) } returns purchase
    val success = ValidationResult.Success(purchase)
    assertEquals(success, request.validate())
    assertEquals(success, request.validate())
  }

  @Test
  fun `validate returns Failure if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
    every { tile.ownersId } returnsMany listOf(UUID.randomUUID(), buyersId)
    val failure = ValidationResult.Failure(tileAlreadyHasOwner)
    assertEquals(failure, request.validate())
    assertEquals(failure, request.validate())
  }

  @Test
  fun `validate returns Failure if the buyer's balance is less than the tile's price`() {
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertEquals(ValidationResult.Failure(buyerHasInsufficientBalance), request.validate())
  }

  @Test
  fun `validate returns Failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(ValidationResult.Failure(invalidPlayerId), request.validate())
  }
}
