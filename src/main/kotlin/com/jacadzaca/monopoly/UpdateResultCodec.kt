package com.jacadzaca.monopoly

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import io.vertx.core.json.*

class UpdateResultCodec : MessageCodec<UpdateResult, UpdateResult> {
  private companion object {
    private const val type = "type"
    private const val reason = "reason"
    private val successJson = JsonObject().put(type, UpdateResult.Success::class.simpleName)
  }
  override fun encodeToWire(buffer: Buffer, s: UpdateResult) {
    when (s) {
      UpdateResult.Success -> successJson
      is UpdateResult.Failure -> JsonObject()
        .put(type, UpdateResult.Failure::class.simpleName)
        .put(reason, s.reason)
    }.writeToBuffer(buffer)
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): UpdateResult {
    val json = buffer.toJsonObject()
    return when (json.getString(type)) {
      UpdateResult.Success::class.simpleName -> UpdateResult.Success
      UpdateResult.Failure::class.simpleName -> UpdateResult.Failure(json.getString(reason))
      else -> throw IllegalStateException("Cannot find UpdateResult of type ${json.getString(type)}")
    }
  }

  override fun transform(s: UpdateResult): UpdateResult = s

  override fun name(): String = this::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
