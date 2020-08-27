package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.marshallers.*
import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import io.vertx.core.json.*
import io.vertx.kotlin.core.json.*

class GameRoomCodec : MessageCodec<GameRoom, GameRoom> {
  private companion object {
    private const val version = "version"
    private const val gameState = "game-state"
  }

  override fun encodeToWire(buffer: Buffer, s: GameRoom) {
    JsonObject()
      .put(version, s.version)
      .put(gameState, JsonGameStateMarshaller.encode(s.gameState))
      .writeToBuffer(buffer)
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): GameRoom {
    val json = buffer.toJsonObject()
    return GameRoom(JsonGameStateMarshaller.decode(json.getJsonObject(gameState)), json.getLong(version))
  }

  override fun transform(s: GameRoom): GameRoom = s

  override fun name(): String = this::class.simpleName.toString()

  override fun systemCodecID(): Byte = -1
}
