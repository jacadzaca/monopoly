package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class JsonGameStateMarshallerTest {
  private val marshaller = JsonGameStateMarshaller
  private val gameState = GameState(createPlayers(), createTiles(), createEvents())

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(gameState, marshaller.decode(marshaller.encode(gameState)))
  }

  private fun createPlayers(): PersistentMap<UUID, Player> {
    val pairs = (1..10)
      .map { Pair(UUID.randomUUID(), Player(Random.nextPositive(), Random.nextPositive().toBigInteger())) }
    return persistentMapOf(*pairs.toTypedArray())
  }

  private fun createTiles(): PersistentList<Tile> {
    return (1..10)
      .map { Tile(createHouses(), createHotels(), Random.nextPositive().toBigInteger(), UUID.randomUUID()) }
      .toPersistentList()
  }

  private fun createEvents(): PersistentList<Event> {
    return (1..10)
      .map { Event.PlayerMoved(UUID.randomUUID(), Random.nextPositive()) }
      .toPersistentList()
  }
}
