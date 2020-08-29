package com.jacadzaca.monopoly

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

object FailureResultCodec : MessageCodec<UpdateResult.Failure, UpdateResult.Failure> {
  override fun encodeToWire(buffer: Buffer, s: UpdateResult.Failure) {
    buffer.appendString(Json.encodeToString(s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): UpdateResult.Failure {
    return Json.decodeFromString(buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: UpdateResult.Failure): UpdateResult.Failure = s

  override fun name(): String = FailureResultCodec::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
