package com.jacadzaca.monopoly

import io.vertx.codegen.annotations.DataObject
import io.vertx.core.json.JsonObject

@DataObject
data class Player(val piece: Piece) {
  constructor(json: JsonObject) : this(Piece(position = json.getInteger("position")))

  fun toJson(): JsonObject {
    return JsonObject()
      .put("position", piece.position)
  }
}
