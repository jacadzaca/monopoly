package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*

object JsonTileEncoder : Encoder<Tile, JsonObject> {
  override fun encode(obj: Tile): JsonObject {
    return JsonObject()
      .put("price", obj.price.toString())
      .put("ownersId", obj.ownersId.toString())
      .put("houses", JsonArray(obj.houses.map(JsonEstateEncoder::encode)))
      .put("hotels", JsonArray(obj.hotels.map(JsonEstateEncoder::encode)))
  }
}
