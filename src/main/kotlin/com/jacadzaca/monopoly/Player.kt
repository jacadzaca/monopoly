package com.jacadzaca.monopoly

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject
import java.util.UUID

@DataObject
data class Player(val id: UUID, val piece: Piece) {
  constructor(json: JsonObject) : this(
    UUID.fromString(json.getString("id")),
    Piece(position = json.getInteger("position")))

  fun toJson(): JsonObject {
    return JsonObject()
      .put("position", piece.position)
  }
}
