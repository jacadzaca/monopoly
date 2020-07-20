package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*

internal class TilePurchaseEventVerifierTest {
  private val tile = mockk<Tile>()
  private val buyer = getTestPlayer()
  private val gameState = mockk<GameState>()
  private val event = TilePurchaseEvent(buyer.id, tileIndex = 0)
  private val eventVerifier: TilePurchaseEventVerifier = TilePurchaseEventVerifier()

  @BeforeEach
  fun setUp() {
    every { gameState.players.getValue(buyer.id) } returns buyer
    every { gameState.tiles[event.tileIndex] } returns tile
  }

  @Test
  fun `verify returns inputted event if the buyer has sufficient funds and the tile has no owner`() {
    every { tile.owner } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    assertSame(event, eventVerifier.verify(event, gameState))
    assertSame(event, eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns null if the the tile already has an owner`() {
    every { tile.owner } returnsMany listOf(UUID.randomUUID(), buyer.id)
    assertNull(eventVerifier.verify(event, gameState))
    assertNull(eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns null if the buyer's balance is less than the tile's price`() {
    every { tile.owner } returns null
    every { tile.price } returns buyer.balance + BigInteger.ONE
    assertNull(eventVerifier.verify(event, gameState))
  }
}
