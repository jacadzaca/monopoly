package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonPlayerMarshaller : Marshaller<Player, JsonObject> {
  override fun encode(obj: Player): JsonObject {
    return JsonObject()
      .put("position", obj.position)
      .put("balance", obj.balance.toString())
  }
}
