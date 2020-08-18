package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.json.*
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<JsonObject> {
  internal companion object {
    internal const val missingType = "Json missing type [string] field"
    internal const val unknownType = "Invalid value in type field"
  }

  override fun parse(raw: JsonObject, playersId: UUID, gameState: GameState): ParsingResult {
    return if (!raw.containsKey("type")) {
      ParsingResult.Failure(missingType)
    } else {
      when (raw.getString("type")) {
        "move" -> ParsingResult.Success(requestFactory.playerMoveRequest(playersId, gameState))
        "tile-purchase" -> ParsingResult.Success(requestFactory.tilePurchaseRequest(playersId, gameState))
        "house-purchase" -> ParsingResult.Success(requestFactory.housePurchaseRequest(playersId, gameState))
        "hotel-purchase" -> ParsingResult.Success(requestFactory.hotelPurchaseRequest(playersId, gameState))
        else -> ParsingResult.Failure(unknownType)
      }
    }
  }
}
