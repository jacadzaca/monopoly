package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class JsonTileMarshallerTest {
  private val marshaller = JsonTileMarshaller
  private val tile = Tile(createHouses(), createHotels(), Random.nextInt().toBigInteger(), UUID.randomUUID())

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(tile, marshaller.decode(marshaller.encode(tile)))
  }
}
