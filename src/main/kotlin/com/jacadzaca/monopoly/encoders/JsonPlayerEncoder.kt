package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonPlayerEncoder : Encoder<Player, JsonObject> {
  override fun encode(obj: Player): JsonObject =
    JsonObject().put("balance", obj.toString()).put("position", obj.position)
}
