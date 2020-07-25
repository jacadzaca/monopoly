package com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.buyerHasInsufficientBalance
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidPlayerId
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEventVerifier.Companion.invalidTileIndex
import com.jacadzaca.monopoly.gamelogic.gamestate.events.VerificationResult
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.createPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*

internal class TilePurchaseEventVerifierTest {
  private val tile = mockk<Tile>()
  private val buyer = createPlayer()
  private val gameState = mockk<GameState>()
  private val event =
    TilePurchaseEvent(
      UUID.randomUUID(),
      tileIndex = 0
    )
  private val verifiedEvent =
    VerificationResult.VerifiedTilePurchaseEvent(
      buyer,
      event.buyerId,
      tile,
      0
    )
  private val tileExists = mockk<(Int, GameState) -> Boolean>()
  private val eventVerifier: TilePurchaseEventVerifier =
    TilePurchaseEventVerifier(tileExists)

  @BeforeEach
  fun setUp() {
    every { gameState.players[event.buyerId] } returns buyer
    every { gameState.tiles[event.tileIndex] } returns tile
    every { tileExists(event.tileIndex, gameState) } returns true
  }

  @Test
  fun `verify returns inputted event if the buyer has sufficient funds and the tile has no owner`() {
    every { tile.owner } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    assertEquals(verifiedEvent, eventVerifier.verify(event, gameState))
    assertEquals(verifiedEvent, eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns Failure if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
    every { tile.owner } returnsMany listOf(UUID.randomUUID(), event.buyerId)
    val failure = VerificationResult.Failure(
      TilePurchaseEventVerifier.tileAlreadyHasOwner
    )
    assertEquals(failure, eventVerifier.verify(event, gameState))
    assertEquals(failure, eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns Failure if the buyer's balance is less than the tile's price`() {
    every { tile.owner } returns null
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertEquals(
      VerificationResult.Failure(
        buyerHasInsufficientBalance
      ),
      eventVerifier.verify(event, gameState)
    )
  }

  @Test
  fun `verify returns Failure if the event references an non-existing player`() {
    every { gameState.players[event.buyerId] } returns null
    assertEquals(
      VerificationResult.Failure(invalidPlayerId),
      eventVerifier.verify(event, gameState)
    )
  }

  @Test
  fun `verify returns Failure if the event references a non-existing tile`() {
    every { tileExists(event.tileIndex, gameState) } returns false
    assertEquals(
      VerificationResult.Failure(invalidTileIndex),
      eventVerifier.verify(event, gameState)
    )
  }
}
