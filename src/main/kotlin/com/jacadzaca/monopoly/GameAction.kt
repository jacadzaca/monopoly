package com.jacadzaca.monopoly

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.Json
import io.vertx.kotlin.core.json.obj
import java.util.UUID

data class GameAction(val committerId: UUID, val moveSize: Int) {
  companion object {
    @JvmStatic
    fun isValidJson(json: JsonObject): Boolean {
      return json.containsKey("committerId") && json.containsKey("moveSize")
    }

    @JvmStatic
    fun creteFromList(list: List<String>): GameAction {
      return GameAction(committerId = UUID.fromString(list[0]), moveSize = list[1].toInt())
    }
  }

  constructor(json: JsonObject) : this(
    UUID.fromString(json.getString("committerId")),
    json.getInteger("moveSize"))

  fun toJson(): JsonObject {
    return JsonObject()
      .put("committerId", committerId)
      .put("moveSize", moveSize)
  }
}
