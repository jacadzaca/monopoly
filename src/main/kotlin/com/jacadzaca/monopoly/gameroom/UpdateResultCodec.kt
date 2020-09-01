package com.jacadzaca.monopoly.gameroom

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*

object UpdateResultCodec : MessageCodec<UpdateResult, UpdateResult> {
  private const val SUCCESS: Byte = 0
  private const val OTHER_CHANGE: Byte = 1

  override fun encodeToWire(buffer: Buffer, s: UpdateResult) {
    when (s) {
      UpdateResult.SUCCESS -> buffer.appendByte(SUCCESS)
      UpdateResult.ALREADY_CHANGED -> buffer.appendByte(OTHER_CHANGE)
    }
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): UpdateResult {
    return when (val byte = buffer.getByte(pos)) {
      SUCCESS -> UpdateResult.SUCCESS
      OTHER_CHANGE -> UpdateResult.ALREADY_CHANGED
      else -> throw IllegalArgumentException("Cannot decode UpdateResult, byte=$byte")
    }
  }

  override fun transform(s: UpdateResult): UpdateResult = s

  override fun name(): String = UpdateResultCodec::class.simpleName!!

  override fun systemCodecID(): Byte = -1
}
