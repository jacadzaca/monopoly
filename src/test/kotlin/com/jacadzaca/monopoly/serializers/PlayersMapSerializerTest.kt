package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class PlayersMapSerializerTest {
  private val map = persistentMapOf(
    UUID.randomUUID() to Player(Random.nextPositive(), Random.nextPositive().toBigInteger()),
    UUID.randomUUID() to Player(Random.nextPositive(), Random.nextPositive().toBigInteger()),
  )

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(map, Json.decodeFromString(PlayersMapSerializer, Json.encodeToString(PlayersMapSerializer, map)))
  }
}
