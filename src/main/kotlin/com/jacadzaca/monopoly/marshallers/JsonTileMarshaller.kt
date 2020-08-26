package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonTileMarshaller : Marshaller<Tile, JsonObject> {
  override fun encode(obj: Tile): JsonObject {
    return JsonObject()
      .put("price", obj.price.toString())
      .put("ownersId", obj.ownersId.toString())
      .put("houses", JsonArray(obj.houses.map(JsonEstateMarshaller::encode)))
      .put("hotels", JsonArray(obj.hotels.map(JsonEstateMarshaller::encode)))
  }
}
