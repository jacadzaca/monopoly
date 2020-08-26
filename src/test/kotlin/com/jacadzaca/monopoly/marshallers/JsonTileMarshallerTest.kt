package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
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

  private fun createHouses(): PersistentList<Estate> {
    return (1..10)
      .map { Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
      .toPersistentList()
  }

  private fun createHotels(): PersistentList<Estate> {
    return (1..10)
      .map { Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
      .toPersistentList()
  }
}
