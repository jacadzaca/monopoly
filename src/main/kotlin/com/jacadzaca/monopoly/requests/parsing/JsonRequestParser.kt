package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.requests.RequestFactory
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<JsonObject> {
  internal companion object {
    internal const val missingType = "Json missing type [string] field"
    internal const val unknownType = "Invalid value in type field"
    internal const val missingPlayerId = "Json missing player-id [uuid] field"
    internal const val missingGameStateId = "Json is missing a game-state-id [uuid] field"
  }

  override fun parse(raw: JsonObject): ParsingResult {
    return if (!raw.containsKey("type")) {
      ParsingResult.Failure(missingType)
    } else if (!raw.containsKey("player-id")) {
      ParsingResult.Failure(missingPlayerId)
    } else if (!raw.containsKey("game-state-id")) {
      ParsingResult.Failure(missingGameStateId)
    } else {
      val playerId = raw.get<UUID>("player-id")
      val gameStateId = raw.get<UUID>("game-state-id")
      when (raw.getString("type")) {
        "move" -> ParsingResult.Success(requestFactory.playerMoveRequest(playerId, gameStateId))
        "tile-purchase" -> ParsingResult.Success(requestFactory.tilePurchaseRequest(playerId, gameStateId))
        "house-purchase" -> ParsingResult.Success(requestFactory.housePurchaseRequest(playerId, gameStateId))
        "hotel-purchase" -> ParsingResult.Success(requestFactory.hotelPurchaseRequest(playerId, gameStateId))
        else -> ParsingResult.Failure(unknownType)
      }
    }
  }
}
