package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class GameStateSerializerTest {
  private val gameState = GameState(
    persistentMapOf(
      UUID.randomUUID() to Player(
        Random.nextPositive(),
        Random.nextPositive().toBigInteger()
      )
    ),
    persistentListOf(
      Tile(
        persistentListOf(Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
        persistentListOf(Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
        Random.nextPositive().toBigInteger(),
        UUID.randomUUID()
      )
    ),
  )

  @Test
  fun `encode method is the inverse of the decode method`() {
    assertEquals(
      gameState,
      Json.decodeFromString(GameStateSerializer, Json.encodeToString(GameStateSerializer, gameState))
    )
  }

  @Test
  fun `decode throws SerializationException if no players field is specified`() {
    assertThrows<SerializationException> {
      Json.decodeFromString(GameStateSerializer, "{\"tiles\": [], \"events\": []}")
    }
  }

  @Test
  fun `decode throws SerializationException if no tiles field is specified`() {
    assertThrows<SerializationException> {
      Json.decodeFromString(GameStateSerializer, "{\"players\": {}, \"events\": []}")
    }
  }
}
