package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class ComputationCodec<T> : MessageCodec<Computation<T>, Computation<T>> {
  override fun encodeToWire(buffer: Buffer, s: Computation<T>) {
    buffer.appendString(Json.encodeToString(s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): Computation<T> {
    return Json.decodeFromString(buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: Computation<T>): Computation<T> = s

  override fun name(): String = this::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
