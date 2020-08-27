package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import java.math.*

object JsonEstateMarshaller {
  fun encode(obj: Estate): JsonObject {
    return JsonObject()
      .put("type", obj::class.simpleName)
      .put("price", obj.price.toString())
      .put("rent", obj.rent.toString())
  }

  fun decode(raw: JsonObject): Estate {
    return when (raw.getString("type")) {
      Estate.House::class.simpleName -> Estate.House(
        BigInteger(raw.getString("rent")),
        BigInteger(raw.getString("price"))
      )
      Estate.Hotel::class.simpleName -> Estate.Hotel(
        BigInteger(raw.getString("rent")),
        BigInteger(raw.getString("price"))
      )
      else -> throw IllegalStateException("No estate of type ${raw.getString("type")}")
    }
  }
}
