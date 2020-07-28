package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

internal class TilePurchaseRequestTest {
  private val tile = mockk<Tile>()
  private val buyer = mockk<Player>()
  private val gameState = mockk<GameState>()
  private val buyersId = UUID.randomUUID()
  private val buyersPosition = Random.nextInt()
  private val request = TilePurchaseRequest(buyersId)

  @BeforeEach
  fun setUp() {
    every { tile.ownersId } returns null
    every { buyer.position } returns buyersPosition
    every { gameState.tiles[buyersPosition] } returns tile
    every { gameState.players[buyersId] } returns buyer
    every { buyer.balance } returns Random.nextInt().toBigInteger()
  }

  @Test
  fun `validate returns Success if the buyer has sufficient funds and the tile has no owner`() {
    every { tile.ownersId } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    val success = ValidationResult.Success(TilePurchase(buyer, buyersId, tile, buyer.position))
    assertEquals(success, request.validate(gameState))
    assertEquals(success, request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
    every { tile.ownersId } returnsMany listOf(UUID.randomUUID(), buyersId)
    val failure = ValidationResult.Failure(TilePurchaseRequest.tileAlreadyHasOwner)
    assertEquals(failure, request.validate(gameState))
    assertEquals(failure, request.validate(gameState))
  }

  @Test
  fun `validate returns Failure if the buyer's balance is less than the tile's price`() {
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertEquals(
      ValidationResult.Failure(Request.buyerHasInsufficientBalance),
      request.validate(gameState)
    )
  }

  @Test
  fun `validate returns Failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(
      ValidationResult.Failure(Request.invalidPlayerId),
      request.validate(gameState)
    )
  }
}
