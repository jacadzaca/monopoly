package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.json.*
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<String> {
  override fun parse(raw: String, playersId: UUID, gameState: GameState): Computation<Request> {
    return parseJson(raw)
      .map { json ->
        if (!json.containsKey("type"))  return RequestParser.MISSING_TYPE
        when (json.getString("type")) {
          "move" -> Computation.success(requestFactory.playerMoveRequest(playersId, gameState))
          "tile-purchase" -> Computation.success(requestFactory.tilePurchaseRequest(playersId, gameState))
          "house-purchase" -> Computation.success(requestFactory.housePurchaseRequest(playersId, gameState))
          "hotel-purchase" -> Computation.success(requestFactory.hotelPurchaseRequest(playersId, gameState))
          else -> RequestParser.UNKNOWN_TYPE
        }
      }
  }

  private fun parseJson(raw: String): Computation<JsonObject> {
    return try {
      Computation.success(JsonObject(raw))
    } catch (e: DecodeException) {
      Computation.failure("JSON parsing error: ${e.message}")
    }
  }
}
