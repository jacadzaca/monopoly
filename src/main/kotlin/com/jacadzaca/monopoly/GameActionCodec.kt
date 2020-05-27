package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.GameEvent
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

class GameActionCodec : MessageCodec<GameEvent, GameEvent> {
  override fun decodeFromWire(pos: Int, buffer: Buffer): GameEvent =
      GameEvent(buffer.toJsonObject())

  override fun systemCodecID(): Byte = -1

  override fun encodeToWire(buffer: Buffer, acation: GameEvent) {
    buffer.appendString(acation.toJson().toString())
  }

  override fun transform(gameEvent: GameEvent): GameEvent = gameEvent

  override fun name(): String = this.javaClass.simpleName

}
