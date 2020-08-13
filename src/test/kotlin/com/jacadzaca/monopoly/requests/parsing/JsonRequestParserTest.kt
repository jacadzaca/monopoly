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
  private val json = JsonObject().put("player-id", UUID.randomUUID()).put("game-room-id", UUID.randomUUID())
  private val playerId = json.get<UUID>("player-id")
  private val gameRoomId = json.get<UUID>("game-room-id")
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
  fun `parse returns Failure if json dose not contain a game-room-id field`() {
    val json = JsonObject().put("player-id", playerId).put("type", randomType())
    assertEquals(ParsingResult.Failure(JsonRequestParser.missingGameStateId), parser.parse(json))
  }

  @Test
  fun `parse creates a PlayerMoveRequest if the type is move`() {
    val json = json.put("type", "move")
    val playerMovementRequest = mockk<PlayerMovementRequest>()
    every { requestFactory.playerMoveRequest(playerId, gameRoomId) } returns playerMovementRequest
    assertEquals(ParsingResult.Success(playerMovementRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a tilePurchaseRequest if the type is tile-purchase`() {
    val json = json.put("type", "tile-purchase")
    val tilePurchaseRequest = mockk<TilePurchaseRequest>()
    every { requestFactory.tilePurchaseRequest(playerId, gameRoomId) } returns tilePurchaseRequest
    assertEquals(ParsingResult.Success(tilePurchaseRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a housePurchaseRequest if the type is house-purchase`() {
    val json = json.put("type", "house-purchase")
    val housePurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.housePurchaseRequest(playerId, gameRoomId) } returns housePurchaseRequest
    assertEquals(ParsingResult.Success(housePurchaseRequest), parser.parse(json))
  }

  @Test
  fun `parse creates a hotelPurchaseRequest if the type is hotel-purchase`() {
    val json = json.put("type", "hotel-purchase")
    val hotelPurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.hotelPurchaseRequest(playerId, gameRoomId) } returns hotelPurchaseRequest
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
