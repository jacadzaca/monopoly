package com.jacadzaca.monopoly.marshallers

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
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(typicalTile()),
        Arguments.of(tileWithNoOwner()),
        Arguments.of(tileWithNoHouses()),
        Arguments.of(tileWithNoHotels())
      )
    }

    private fun typicalTile() = Tile(createHouses(), createHotels(), Random.nextInt().toBigInteger(), UUID.randomUUID())

    private fun tileWithNoOwner() = Tile(createHouses(), createHotels(), Random.nextInt().toBigInteger(), null)

    private fun tileWithNoHouses() =
      Tile(persistentListOf(), createHotels(), Random.nextPositive().toBigInteger(), UUID.randomUUID())

    private fun tileWithNoHotels() =
      Tile(createHouses(), persistentListOf(), Random.nextPositive().toBigInteger(), UUID.randomUUID())
  }
}
