package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import io.mockk.*
import io.vertx.core.json.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class JsonRequestParserTest {
  private val requestFactory = mockk<RequestFactory>()
  private val playersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val parser = JsonRequestParser(requestFactory)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
  }

  @Test
  fun `parse returns MissingType if json dose not contain type field`() {
    assertEquals(RequestParser.MISSING_TYPE, parser.parse("{}", playersId, gameState))
  }

  @Test
  fun `parse returns UnknownType if json specifies an unknown type`() {
    val json = "{\"type\": \"${randomString()}\"}"
    assertEquals(RequestParser.UNKNOWN_TYPE, parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a PlayerMoveRequest if the type is move`() {
    val json = "{\"type\":\"move\"}"
    val playerMovementRequest = mockk<PlayerMovementRequest>()
    every { requestFactory.playerMoveRequest(playersId, gameState) } returns playerMovementRequest
    assertEquals(Computation.success(playerMovementRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse returns ParsingError if the string is wrong json`() {
    val json = "{null, asdf false true ,,, }"
    assertNotNull(parser.parse(json, playersId, gameState).message)
    assertTrue(parser.parse(json, playersId, gameState).message!!.contains("JSON par"))
    assertNull(parser.parse(json, playersId, gameState).value)
  }

  @Test
  fun `parse creates a tilePurchaseRequest if the type is tile-purchase`() {
    val json = "{\"type\":\"tile-purchase\"}"
    val tilePurchaseRequest = mockk<TilePurchaseRequest>()
    every { requestFactory.tilePurchaseRequest(playersId, gameState) } returns tilePurchaseRequest
    assertEquals(Computation.success(tilePurchaseRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a housePurchaseRequest if the type is house-purchase`() {
    val json = "{\"type\": \"house-purchase\"}"
    val housePurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.housePurchaseRequest(playersId, gameState) } returns housePurchaseRequest
    assertEquals(Computation.success(housePurchaseRequest), parser.parse(json, playersId, gameState))
  }

  @Test
  fun `parse creates a hotelPurchaseRequest if the type is hotel-purchase`() {
    val json = "{\"type\": \"hotel-purchase\"}"
    val hotelPurchaseRequest = mockk<EstatePurchaseRequest>()
    every { requestFactory.hotelPurchaseRequest(playersId, gameState) } returns hotelPurchaseRequest
    assertEquals(Computation.success(hotelPurchaseRequest), parser.parse(json, playersId, gameState))
  }

  private fun randomString(): String {
    return generateSequence { Random.nextInt(Char.MIN_VALUE.toInt(), Char.MAX_VALUE.toInt()) }
      .map(Int::toChar)
      .take(200)
      .toString()
  }
}
