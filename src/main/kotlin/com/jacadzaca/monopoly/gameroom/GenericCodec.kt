package com.jacadzaca.monopoly.gameroom

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class GenericCodec<T>(private val serializer: KSerializer<T>) : MessageCodec<T, T> {
  override fun encodeToWire(buffer: Buffer, s: T) {
    buffer.appendString(Json.encodeToString(serializer, s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): T {
    return Json.decodeFromString(serializer, buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: T): T = s

  override fun name(): String = "${this::class.simpleName} ${serializer::class.simpleName}"

  override fun systemCodecID(): Byte = -1
}
