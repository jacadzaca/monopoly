package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import java.util.*

object JsonGameStateMarshaller : Marshaller<GameState, JsonObject> {
  override fun encode(obj: GameState): JsonObject {
    return JsonObject()
      .put("players", JsonArray(obj.players.map(::encodeEntry)))
      .put("tiles", JsonArray(obj.tiles.map(JsonTileMarshaller::encode)))
      .put("events", JsonArray(obj.events.map(JsonEventMarshaller::encode)))
  }

  private fun encodeEntry(entry: Map.Entry<UUID, Player>): JsonObject {
    return JsonObject()
      .put("id", entry.key)
      .put("player", JsonPlayerMarshaller.encode(entry.value))
  }
}
