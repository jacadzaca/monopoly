package com.jacadzaca.monopoly

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

object SuccessResultCodec : MessageCodec<UpdateResult.Success, UpdateResult.Success> {
  override fun encodeToWire(buffer: Buffer, s: UpdateResult.Success) {
    buffer.appendString(Json.encodeToString(s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): UpdateResult.Success {
    return Json.decodeFromString(buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: UpdateResult.Success): UpdateResult.Success = s

  override fun name(): String = SuccessResultCodec::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
