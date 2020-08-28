package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.*

internal class PersistentEstateListSerializerTest {
  private val estates =
    (1..10)
      .map { Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
      .plus((1..10).map { Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) })
      .toPersistentList()

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(
      estates,
      Json.decodeFromString(
        PersistentEstateListSerializer,
        Json.encodeToString(PersistentEstateListSerializer, estates)
      )
    )
  }
}
