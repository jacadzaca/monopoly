package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import java.util.*

object JsonGameStateEncoder : Encoder<GameState, JsonObject> {
  override fun encode(obj: GameState): JsonObject {
    return JsonObject()
      .put("players", JsonArray(obj.players.map(::encodeEntry)))
      .put("tiles", JsonArray(obj.tiles.map(JsonTileEncoder::encode)))
      .put("events", JsonArray(obj.events.map(JsonEventEncoder::encode)))
  }

  private fun encodeEntry(entry: Map.Entry<UUID, Player>): JsonObject {
    return JsonObject()
      .put("id", entry.key)
      .put("player", JsonPlayerEncoder.encode(entry.value))
  }
}
