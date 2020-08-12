package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.requests.*
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import java.util.*
import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class JsonRequestParserTest {
  private val requestFactory = mockk<RequestFactory>()
  private val json = JsonObject().put("player-id", UUID.randomUUID()).put("game-state-id", UUID.randomUUID())
  private val playerId = json.get<UUID>("player-id")
  private val gameStatesId = json.get<UUID>("game-state-id")
  private val parser = JsonRequestParser(requestFactory)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
  }

  @Test
  fun `parse returns Failure if json dose not contain type field`() {
    assertEquals(ParsingResult.Failure(JsonRequestParser.missingType), parser.parse(json))
  }

  @Test
  fun `parse returns Failure if json specifies an unknown type`() {
    val json = json.put("type", randomString())
    assertEquals(ParsingResult.Failure(JsonRequestParser.unknownType), parser.parse(json))
  }

  @Test
  fun `parse returns Failure if json dose not contain player-id field`() {
    val json = JsonObject().put("type", randomType())
    assertEquals(ParsingResult.Failure(JsonRequestParser.missingPlayerId), parser.parse(json))
  }

  @Test
  fun `parse returns Failure if json dose not contain a game-state-id field`() {
    val json = JsonObject().put("player-id", playerId).put("type", randomType())
    assertEquals(ParsingResult.Failure(JsonRequestParser.missingGameStateId), parser.parse(json))
  }

  @Test
  fun `parse creates a PlayerMoveRequest if the type is move`() {
    val json = json.put("type", "move")
    val playerMovementRequest = mockk<Request>()
    every { requestFactory.playerMoveRequest(playerId, gameStatesId) } returns playerMovementRequest
    assertEquals(ParsingResult.Success(playerMovementRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a tilePurchaseRequest if the type is tile-purchase`() {
    val json = json.put("type", "tile-purchase")
    val tilePurchaseRequest = mockk<Request>()
    every { requestFactory.tilePurchaseRequest(playerId, gameStatesId) } returns tilePurchaseRequest
    assertEquals(ParsingResult.Success(tilePurchaseRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a housePurchaseRequest if the type is house-purchase`() {
    val json = json.put("type", "house-purchase")
    val housePurchaseRequest = mockk<Request>()
    every { requestFactory.housePurchaseRequest(playerId, gameStatesId) } returns housePurchaseRequest
    assertEquals(ParsingResult.Success(housePurchaseRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a hotelPurchaseRequest if the type is hotel-purchase`() {
    val json = json.put("type", "hotel-purchase")
    val hotelPurchaseRequest = mockk<Request>()
    every { requestFactory.hotelPurchaseRequest(playerId, gameStatesId) } returns hotelPurchaseRequest
    assertEquals(ParsingResult.Success(hotelPurchaseRequest), parser.parse(json))
  }

  private fun randomString(): String {
    return generateSequence { Random.nextInt(Char.MIN_VALUE.toInt(), Char.MAX_VALUE.toInt()) }
      .map(Int::toChar)
      .take(200)
      .toString()
  }

  private fun randomType(): String = listOf("move", "tile-purchase", "house-purchase", "hotel-purchase").random()
}
