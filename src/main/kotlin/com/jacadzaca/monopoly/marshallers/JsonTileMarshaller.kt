package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import kotlinx.collections.immutable.*
import java.math.*
import java.util.*

object JsonTileMarshaller {
  private const val price = "price"
  private const val ownersId = "owners-id"
  private const val houses = "houses"
  private const val hotels = "hotels"

  fun encode(obj: Tile): JsonObject {
    return JsonObject()
      .put(price, obj.price.toString())
      .put(ownersId, obj.ownersId.toString())
      .put(houses, JsonArray(obj.houses.map(JsonEstateMarshaller::encode)))
      .put(hotels, JsonArray(obj.hotels.map(JsonEstateMarshaller::encode)))
  }

  fun decode(raw: JsonObject): Tile {
    val houses = raw.getJsonArray(houses)
      .map { it as JsonObject }
      .map(JsonEstateMarshaller::decode)
      .toPersistentList()
    val hotels = raw.getJsonArray(hotels)
      .map { it as JsonObject }
      .map(JsonEstateMarshaller::decode)
      .toPersistentList()
    return Tile(houses, hotels, BigInteger(raw.getString(price)), UUID.fromString(raw.getString(ownersId)))
  }
}
