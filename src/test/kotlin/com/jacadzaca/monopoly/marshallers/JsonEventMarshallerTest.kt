package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import io.vertx.core.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.util.*
import java.util.stream.*
import kotlin.random.Random

internal class JsonEventMarshallerTest {
  private val marshaller = JsonEventMarshaller

  @ParameterizedTest
  @ArgumentsSource(EventSource::class)
  fun `decode method is the inverse of the encode method`(event: Event) {
    assertEquals(event, marshaller.decode(marshaller.encode(event)))
  }

  @Test
  fun `decode throws IllegalStateArgument if type is unknown`() {
    assertThrows<IllegalStateException> {
      marshaller.decode(JsonObject().put("type", Random.nextString()))
    }
  }

  private object EventSource : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(Event.PlayerMoved(UUID.randomUUID(), Random.nextPositive())),
        Arguments.of(Event.TilePurchased(UUID.randomUUID(), Random.nextPositive())),
        Arguments.of(
          Event.LiabilityPaid(UUID.randomUUID(), UUID.randomUUID(), Random.nextPositive().toBigInteger())
        ),
        Arguments.of(
          Event.EstatePurchased(
            UUID.randomUUID(),
            Random.nextPositive(),
            Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())
          )
        ),
        Arguments.of(
          Event.EstatePurchased(
            UUID.randomUUID(),
            Random.nextPositive(),
            Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())
          )
        )
      )
    }
  }
}
