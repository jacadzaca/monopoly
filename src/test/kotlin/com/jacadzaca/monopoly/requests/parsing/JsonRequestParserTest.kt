package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.requests.*
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class JsonRequestParserTest {
  private val requestFactory = mockk<RequestFactory>()
  private val json = JsonObject().put("player-id", UUID.randomUUID())
  private val playerId = json.get<UUID>("player-id")
  private val parser = JsonRequestParser(requestFactory)

  @BeforeEach
  fun setUp() {
    clearMocks(requestFactory)
  }

  @Test
  fun `parse returns Failure if json dose not contain type field`() {
    assertEquals(
      ParsingResult.Failure(JsonRequestParser.missingType),
      parser.parse(json)
    )
  }

  @Test
  fun `parse returns Failure if json specifies an unknown type`() {
    val json = json.put("type", randomString())
    assertEquals(
      ParsingResult.Failure(JsonRequestParser.unknownType),
      parser.parse(json)
    )
  }

  @Test
  fun `parse returns Failure if json dose not contain player-id field`() {
    val json = JsonObject().put("type", randomType())
    assertEquals(
      ParsingResult.Failure(JsonRequestParser.missingPlayerId),
      parser.parse(json)
    )
  }

  @Test
  fun `parse returns the result of moveParser if the type is move`() {
    val json = json.put("type", "move")
    val playerMovementRequest = mockk<Request>(name = "playerMovementRequest")
    every { requestFactory.instanceOf(playerId, PlayerMovementRequest::class) } returns playerMovementRequest
    assertEquals(
      ParsingResult.Success(playerMovementRequest),
      parser.parse(json)
    )
  }

  @Test
  fun `parse returns result of tilePurchaseParser if the type is tilePurchase`() {
    val json = json.put("type", "tilePurchase")
    val tilePurchaseRequest = mockk<Request>(name = "tilePurchaseRequest")
    every { requestFactory.instanceOf(playerId, TilePurchaseRequest::class) } returns tilePurchaseRequest
    assertEquals(
      ParsingResult.Success(tilePurchaseRequest),
      parser.parse(json)
    )
  }

  @Test
  fun `parse returns the EstatePurchaseRequest if the type is estatePurchase`() {
    val json = json.put("type", "estatePurchase")
    val estatePurchaseRequest = mockk<Request>(name = "estatePurchaseRequest")
    every { requestFactory.instanceOf(playerId, EstatePurchaseRequest::class) } returns estatePurchaseRequest
    assertEquals(
      ParsingResult.Success(estatePurchaseRequest),
      parser.parse(json)
    )
  }

  private fun randomString(): String {
    return generateSequence { Random.nextInt(Char.MIN_VALUE.toInt(), Char.MAX_VALUE.toInt()) }
      .map(Int::toChar)
      .take(200)
      .toString()
  }

  private fun randomType(): String = listOf("move", "tilePurchase", "estatePurchase").random()
}
