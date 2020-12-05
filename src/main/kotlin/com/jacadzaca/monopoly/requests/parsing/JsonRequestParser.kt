package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.json.*
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<JsonObject> {
  override fun parse(raw: JsonObject, playersId: UUID, gameState: GameState): Computation<Request> {
    return if (!raw.containsKey("type")) {
      RequestParser.MISSING_TYPE
    } else {
      when (raw.getString("type")) {
        "move" -> Computation.success(requestFactory.playerMoveRequest(playersId, gameState))
        "tile-purchase" -> Computation.success(requestFactory.tilePurchaseRequest(playersId, gameState))
        "house-purchase" -> Computation.success(requestFactory.housePurchaseRequest(playersId, gameState))
        "hotel-purchase" -> Computation.success(requestFactory.hotelPurchaseRequest(playersId, gameState))
        else -> RequestParser.UNKNOWN_TYPE
      }
    }
  }


}
