package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.util.*
import java.util.stream.*
import kotlin.random.Random

internal class TileSerializerTest {
  @ParameterizedTest
  @ArgumentsSource(TileSource::class)
  fun `decode method is the inverse of the encode method`(tile: Tile) {
    assertEquals(
      tile,
      Json.decodeFromString(TileSerializer, Json.encodeToString(TileSerializer, tile))
    )
  }

  private class TileSource : ArgumentsProvider {
    private val houses =
      (1..10)
        .map { Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
        .toPersistentList()
    private val hotels =
      (1..10)
        .map { Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
        .toPersistentList()

    override

    fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(typicalTile()),
        Arguments.of(tileWithNoOwner()),
        Arguments.of(tileWithNoHouses()),
        Arguments.of(tileWithNoHotels())
      )
    }

    private fun typicalTile() = Tile(houses, hotels, Random.nextPositive().toBigInteger(), UUID.randomUUID())

    private fun tileWithNoOwner() = Tile(houses, hotels, Random.nextPositive().toBigInteger(), null)

    private fun tileWithNoHouses() =
      Tile(persistentListOf(), hotels, Random.nextPositive().toBigInteger(), UUID.randomUUID())

    private fun tileWithNoHotels() =
      Tile(houses, persistentListOf(), Random.nextPositive().toBigInteger(), UUID.randomUUID())
  }
}
