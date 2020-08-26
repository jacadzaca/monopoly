package com.jacadzaca.monopoly.marshallers

import io.vertx.core.json.*

@Suppress("UNCHECKED_CAST")
fun <T> JsonObject.getJsonArrayOfType(key: String): List<T> {
  return getJsonArray(key).map { it as T }
}
