package com.jacadzaca.monopoly

import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class GameRoomCodec : MessageCodec<GameRoom, GameRoom> {
  override fun encodeToWire(buffer: Buffer, s: GameRoom) {
    buffer.appendString(Json.encodeToString(s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): GameRoom =
    Json.decodeFromString(buffer.getString(0, buffer.length()))


  override fun transform(s: GameRoom): GameRoom = s

  override fun name(): String = this::class.simpleName.toString()

  override fun systemCodecID(): Byte = -1
}
