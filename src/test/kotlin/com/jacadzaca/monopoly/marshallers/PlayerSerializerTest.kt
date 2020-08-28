package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.*

internal class PlayerSerializerTest {
  private val player = Player(Random.nextPositive(), Random.nextPositive().toBigInteger())

  @Test
  fun `encode method is the inverse of the decode method`() {
    assertEquals(player, Json.decodeFromString(PlayerSerializer, Json.encodeToString(PlayerSerializer, player)))
  }

  @Test
  fun `decode throws SerializationException if balance is not specified`() {
    assertThrows<SerializationException> {
      Json.decodeFromString(PlayerSerializer, "{\"position\": 1}")
    }
  }

  @Test
  fun `decode throws SerializationException if position is not specified`() {
    assertThrows<SerializationException> {
      Json.decodeFromString(PlayerSerializer, "{\"balance\": \"1\"}")
    }
  }
}
