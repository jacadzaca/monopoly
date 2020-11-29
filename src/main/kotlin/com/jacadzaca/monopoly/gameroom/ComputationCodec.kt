package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class ComputationCodec<T> : MessageCodec<ComputationResult<T>, ComputationResult<T>> {
  override fun encodeToWire(buffer: Buffer, s: ComputationResult<T>) {
    buffer.appendString(Json.encodeToString(s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): ComputationResult<T> {
    return Json.decodeFromString(buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: ComputationResult<T>): ComputationResult<T> = s

  override fun name(): String = this::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
