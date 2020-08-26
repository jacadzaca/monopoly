package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonPlayerEncoder : Encoder<Player, JsonObject> {
  override fun encode(obj: Player): JsonObject {
    return JsonObject()
      .put("position", obj.position)
      .put("balance", obj.balance.toString())
  }
}
