package com.jacadzaca.monopoly.gameroom

import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomCreationVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "create-game-room"
    internal const val ROOMS_NAME = "roomsName"
    internal const val SUCCESS = 0
    internal const val NAME_TAKEN = 1
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx
      .sharedData()
      .getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val messages = vertx
      .eventBus()
      .consumer<GameRoom>(ADDRESS)
      .toChannel(vertx)
    launch {
      for (message in messages) {
        launch {
          val response = rooms.putIfAbsentAwait(message.headers()[ROOMS_NAME], message.body())
          message.reply(if (response == null) SUCCESS else NAME_TAKEN)
        }
      }
    }
    logger.info("Started a ${this::class.qualifiedName} instance")
  }
}
