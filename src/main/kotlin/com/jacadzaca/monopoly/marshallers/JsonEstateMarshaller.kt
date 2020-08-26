package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonEstateMarshaller : Marshaller<Estate, JsonObject> {
  override fun encode(obj: Estate): JsonObject {
    return JsonObject()
      .put("price", obj.price)
      .put("rent", obj.rent)
  }
}
