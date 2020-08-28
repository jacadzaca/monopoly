package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class PersistentEventListSerializerTest {
  private val list = persistentListOf(Event.PlayerMoved(UUID.randomUUID(), Random.nextPositive()))

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(
      list,
      Json.decodeFromString(PersistentEventListSerializer, Json.encodeToString(PersistentEventListSerializer, list))
    )
  }
}
