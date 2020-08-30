package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.json.*
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<JsonObject> {
  internal companion object {
    internal val missingType = IllegalArgumentException("Json missing type [string] field")
    internal val unknownType = IllegalArgumentException("Invalid value in type field")
  }

  override fun parse(raw: JsonObject, playersId: UUID, gameState: GameState): Result<Request> {
    return if (!raw.containsKey("type")) {
      Result.failure(missingType)
    } else {
      when (raw.getString("type")) {
        "move" -> Result.success(requestFactory.playerMoveRequest(playersId, gameState))
        "tile-purchase" -> Result.success(requestFactory.tilePurchaseRequest(playersId, gameState))
        "house-purchase" -> Result.success(requestFactory.housePurchaseRequest(playersId, gameState))
        "hotel-purchase" -> Result.success(requestFactory.hotelPurchaseRequest(playersId, gameState))
        else -> Result.failure(unknownType)
      }
    }
  }
}
