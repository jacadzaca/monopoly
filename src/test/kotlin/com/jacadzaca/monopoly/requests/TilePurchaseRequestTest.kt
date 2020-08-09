package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.transformations.TilePurchase
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
  private val request = TilePurchaseRequest(buyersId, gameState)

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
    val success = ValidationResult.Success(TilePurchase(buyer, buyersId, tile, buyer.position, gameState))
    assertEquals(success, request.validate())
    assertEquals(success, request.validate())
  }

  @Test
  fun `validate returns Failure if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
    every { tile.ownersId } returnsMany listOf(UUID.randomUUID(), buyersId)
    val failure = ValidationResult.Failure(TilePurchaseRequest.tileAlreadyHasOwner)
    assertEquals(failure, request.validate())
    assertEquals(failure, request.validate())
  }

  @Test
  fun `validate returns Failure if the buyer's balance is less than the tile's price`() {
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertEquals(
      ValidationResult.Failure(Request.buyerHasInsufficientBalance),
      request.validate()
    )
  }

  @Test
  fun `validate returns Failure if the event references an non-existing player`() {
    every { gameState.players[buyersId] } returns null
    assertEquals(
      ValidationResult.Failure(Request.invalidPlayerId),
      request.validate()
    )
  }
}
