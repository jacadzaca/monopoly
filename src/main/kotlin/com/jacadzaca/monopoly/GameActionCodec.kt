package com.jacadzaca.monopoly

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

class GameActionCodec : MessageCodec<GameAction, GameAction> {
  override fun decodeFromWire(pos: Int, buffer: Buffer): GameAction = GameAction(buffer.toJsonObject())

  override fun systemCodecID(): Byte = -1

  override fun encodeToWire(buffer: Buffer, acation: GameAction) {
    buffer.appendString(acation.toJson().toString())
  }

  override fun transform(gameAction: GameAction): GameAction = gameAction

  override fun name(): String = this.javaClass.simpleName

}
