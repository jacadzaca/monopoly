package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.util.stream.*
import kotlin.random.*

internal class JsonEstateMarshallerTest {
  private val marshaller = JsonEstateMarshaller

  @ParameterizedTest
  @ArgumentsSource(EstateProvider::class)
  fun `decode method is the inverse of the encode method`(estate: Estate) {
    assertEquals(estate, marshaller.decode(marshaller.encode(estate)))
  }

  @ParameterizedTest
  @ArgumentsSource(EstateProvider::class)
  fun `decode throws IllegalStateException if type is unknown`(estate: Estate) {
    assertThrows<IllegalStateException> {
      marshaller.decode(
        JsonObject()
          .put("type", Random.nextString())
          .put("price", estate.price.toString())
          .put("rent", estate.rent.toString())
      )
    }
  }

  private object EstateProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
        Arguments.of(Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()))
      )
    }
  }
}
