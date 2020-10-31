package com.jacadzaca.monopoly.gameroom

import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomLookupVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "lookup-game-room"
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val deliveryOptions = deliveryOptionsOf(codecName = GameRoomCodec.name())
  }

  override suspend fun start() {
    val rooms = vertx
      .sharedData()
      .getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val messages = vertx
      .eventBus()
      .registerCodec(GameRoomCodec)
      .consumer<String>(ADDRESS)
      .toChannel(vertx)
    launch {
      for (message in messages) {
        launch {
          message.reply(rooms.getAwait(message.body()), deliveryOptions)
        }
      }
    }
    logger.info("Started a ${this::class.qualifiedName} instance")
  }
}
