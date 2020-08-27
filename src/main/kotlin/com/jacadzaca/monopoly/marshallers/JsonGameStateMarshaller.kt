package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import kotlinx.collections.immutable.*
import java.util.*

object JsonGameStateMarshaller {
  private const val id = "id"
  private const val player = "player"
  private const val players = "${player}s"
  private const val tiles = "tiles"
  private const val events = "events"

  fun encode(obj: GameState): JsonObject {
    return JsonObject()
      .put(players, JsonArray(obj.players.map(::encodeEntry)))
      .put(tiles, JsonArray(obj.tiles.map(JsonTileMarshaller::encode)))
      .put(events, JsonArray(obj.events.map(JsonEventMarshaller::encode)))
  }

  private fun encodeEntry(entry: Map.Entry<UUID, Player>): JsonObject {
    return JsonObject()
      .put(id, entry.key.toString())
      .put(player, JsonPlayerMarshaller.encode(entry.value))
  }

  fun decode(raw: JsonObject): GameState {
    val players = persistentHashMapOf(*raw.getJsonArrayOfType<JsonObject>(players).map(::decodeEntry).toTypedArray())
    val tiles = raw
      .getJsonArrayOfType<JsonObject>(tiles)
      .map(JsonTileMarshaller::decode)
      .toPersistentList()
    val events = raw
      .getJsonArrayOfType<JsonObject>(events)
      .map(JsonEventMarshaller::decode)
      .toPersistentList()
    return GameState(players, tiles, events)
  }

  private fun decodeEntry(json: JsonObject): Pair<UUID, Player> {
    return Pair(
      UUID.fromString(json.getString(id)),
      JsonPlayerMarshaller.decode(json.getJsonObject(player))
    )
  }
}
