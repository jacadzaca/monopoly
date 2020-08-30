package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gameroom.codecs.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomLookupVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "lookup-game-room"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx.sharedData().getLocalAsyncMap<String, GameRoom>("game-rooms").await()
    val eventBus = vertx.eventBus().registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
    launch {
      val messages = eventBus.consumer<String>(ADDRESS).toChannel(vertx)
      for (message in messages) {
        message.reply(rooms.getAwait(message.body()))
      }
    }
    logger.info("Started a ${this::class.qualifiedName} instance")
  }
}
