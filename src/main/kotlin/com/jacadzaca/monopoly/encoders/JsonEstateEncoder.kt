package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonEstateEncoder : Encoder<Estate, JsonObject> {
  override fun encode(obj: Estate): JsonObject {
    return JsonObject().put("price", obj.price).put("rent", obj.rent)
  }
}
