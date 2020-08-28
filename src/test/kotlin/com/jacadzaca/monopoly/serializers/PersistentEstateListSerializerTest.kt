package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

internal class PersistentEstateListSerializerTest {
  private val estates = createHouses()

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
