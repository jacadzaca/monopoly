package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.util.stream.*
import kotlin.random.*

internal class EstateSerializerTest {
  @ParameterizedTest
  @ArgumentsSource(EstateProvider::class)
  fun `decode method is the inverse of the encode method`(estate: Estate) {
    assertEquals(estate, Json.decodeFromString<Estate>(Json.encodeToString(estate)))
  }

  private object EstateProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
        Arguments.of(Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()))
      )
    }
  }
}
