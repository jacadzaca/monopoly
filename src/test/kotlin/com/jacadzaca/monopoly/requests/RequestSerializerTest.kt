package com.jacadzaca.monopoly.requests

import io.mockk.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.params.*
import org.junit.jupiter.params.provider.*
import java.util.*
import java.util.stream.*

internal class RequestSerializerTest {
  @ParameterizedTest(name = "decode method is the inverse of the encode method")
  @ArgumentsSource(RequestProvider::class)
  fun `decode method is the inverse of the encode method`(request: Request) {
    assertEquals(
      request.playersId(),
      Json.decodeFromString(RequestSerializer, Json.encodeToString(RequestSerializer, request)).playersId()
    )
    assertEquals(
      request::class,
      Json.decodeFromString(RequestSerializer, Json.encodeToString(RequestSerializer, request))::class
    )
  }

  private class RequestProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(createRequest<PlayerMovementRequest>()),
        Arguments.of(createRequest<TilePurchaseRequest>()),
        Arguments.of(createRequest<HousePurchaseRequest>())
      )
    }

    private inline fun <reified T : Request> createRequest(): Request {
      val request = mockk<T>()
      every { request.playersId() } returns UUID.randomUUID()
      return request
    }
  }
}

