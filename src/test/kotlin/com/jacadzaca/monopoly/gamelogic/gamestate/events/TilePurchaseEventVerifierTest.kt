package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.VerifiedTilePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*

internal class TilePurchaseEventVerifierTest {
  private val tile = mockk<Tile>()
  private val buyer = getTestPlayer()
  private val gameState = mockk<GameState>()
  private val event =
    TilePurchaseEvent(
      buyer.id,
      tileIndex = 0
    )
  private val verifiedEvent = VerifiedTilePurchaseEvent(event)
  private val tileExists = mockk<(Int, GameState) -> Boolean>()
  private val eventVerifier: TilePurchaseEventVerifier =
    TilePurchaseEventVerifier(tileExists)

  @BeforeEach
  fun setUp() {
    every { gameState.players[buyer.id] } returns buyer
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
  fun `verify returns null if the the tile already has an owner`() {
    every { tile.price } returns buyer.balance - BigInteger.ONE
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

  @Test
  fun `verify returns null if the event references an non-existing player`() {
    every { gameState.players[event.playerId] } returns null
    assertNull(eventVerifier.verify(event, gameState))
  }

  @Test
  fun `verify returns null if the event references a non-existing tile`() {
    every { tileExists(event.tileIndex, gameState) } returns false
    every { gameState.tiles[event.tileIndex] } throws IndexOutOfBoundsException()
    assertNull(eventVerifier.verify(event, gameState))
  }
}
