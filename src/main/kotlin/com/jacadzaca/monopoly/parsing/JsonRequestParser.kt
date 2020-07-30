package com.jacadzaca.monopoly.parsing

import com.jacadzaca.monopoly.gamelogic.estatepurchase.EstatePurchaseRequest
import com.jacadzaca.monopoly.gamelogic.moving.PlayerMovementRequest
import com.jacadzaca.monopoly.gamelogic.tilepurchase.TilePurchaseRequest
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import java.util.*

class JsonRequestParser(private val requestFactory: RequestFactory) : RequestParser<JsonObject> {
  internal companion object {
    internal const val missingType = "Json missing type [string] field"
    internal const val unknownType = "Invalid value in type field"
    internal const val missingPlayerId = "Json missing player-id [uuid] field"
  }

  override fun parse(raw: JsonObject): ParsingResult {
    if (!raw.containsKey("type")) {
      return ParsingResult.Failure(missingType)
    } else if (!raw.containsKey("player-id")) {
      return ParsingResult.Failure(missingPlayerId)
    }
    val playerId = raw.get<UUID>("player-id")
    return when (raw.getString("type")) {
      "move" -> ParsingResult.Success(requestFactory.instanceOf(playerId, PlayerMovementRequest::class))
      "tilePurchase" -> ParsingResult.Success(requestFactory.instanceOf(playerId, TilePurchaseRequest::class))
      "estatePurchase" -> ParsingResult.Success(requestFactory.instanceOf(playerId, EstatePurchaseRequest::class))
      else -> ParsingResult.Failure(unknownType)
    }
  }
}
