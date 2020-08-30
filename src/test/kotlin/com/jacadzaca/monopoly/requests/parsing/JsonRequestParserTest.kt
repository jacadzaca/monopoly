package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.parsing.JsonRequestParser.Companion.MISSING_TYPE
import com.jacadzaca.monopoly.requests.parsing.JsonRequestParser.Companion.UNKNOWN_TYPE
import io.mockk.*
import io.vertx.core.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class JsonRequestParserTest {
  private val requestFactory = mockk<RequestFactory>()
  private val json = JsonObject()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val parser = JsonRequestParser(requestFactory)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
  }

  @Test
  fun `parse returns Failure if json dose not contain type field`() {
    assertEquals(MISSING_TYPE, parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse returns Failure if json specifies an unknown type`() {
    val json = json.put("type", randomString())
    assertEquals(UNKNOWN_TYPE, parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a PlayerMoveRequest if the type is move`() {
    val json = json.put("type", "move")
    val playerMovementRequest = mockk<PlayerMovementRequest>()
    every { requestFactory.playerMoveRequest(playersId, gameState) } returns playerMovementRequest
    assertEquals(Result.success(playerMovementRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a tilePurchaseRequest if the type is tile-purchase`() {
    val json = json.put("type", "tile-purchase")
    val tilePurchaseRequest = mockk<TilePurchaseRequest>()
    every { requestFactory.tilePurchaseRequest(playersId, gameState) } returns tilePurchaseRequest
    assertEquals(Result.success(tilePurchaseRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a housePurchaseRequest if the type is house-purchase`() {
    val json = json.put("type", "house-purchase")
    val housePurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.housePurchaseRequest(playersId, gameState) } returns housePurchaseRequest
    assertEquals(Result.success(housePurchaseRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a hotelPurchaseRequest if the type is hotel-purchase`() {
    val json = json.put("type", "hotel-purchase")
    val hotelPurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.hotelPurchaseRequest(playersId, gameState) } returns hotelPurchaseRequest
    assertEquals(Result.success(hotelPurchaseRequest), parser.parse(json, playersId, gameState))
  }

  private fun randomString(): String {
    return generateSequence { Random.nextInt(Char.MIN_VALUE.toInt(), Char.MAX_VALUE.toInt()) }
      .map(Int::toChar)
      .take(200)
      .toString()
  }
}
