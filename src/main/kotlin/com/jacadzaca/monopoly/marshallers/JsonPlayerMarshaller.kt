package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.json.*
import java.math.*

object JsonPlayerMarshaller {
  private const val position = "position"
  private const val balance = "balance"

  fun encode(obj: Player): JsonObject {
    return JsonObject()
      .put(position, obj.position)
      .put(balance, obj.balance.toString())
  }

  fun decode(raw: JsonObject): Player = Player(raw.getInteger(position), BigInteger(raw.getString(balance)))
}
