package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomLookupVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "lookup-game-room"
    private val NO_ROOM_WITH_NAME = Computation.failure<GameRoom>("No room with such name")
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx
      .sharedData()
      .getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val messages = vertx
      .eventBus()
      .consumer<String>(ADDRESS)
      .toChannel(vertx)
    launch {
      for (message in messages) {
        launch {
          val room = rooms.getAwait(message.body())
          message.reply(if (room != null) Computation.success(room) else NO_ROOM_WITH_NAME)
        }
      }
    }
    logger.info("Started a ${this::class.qualifiedName} instance")
  }
}
