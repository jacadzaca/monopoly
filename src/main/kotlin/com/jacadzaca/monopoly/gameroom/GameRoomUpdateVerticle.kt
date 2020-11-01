package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomUpdateVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "update-game-room"
    internal const val SUCCESS = 0
    internal const val NO_ROOM_WITH_NAME = 1
    internal const val ALREADY_CHANGED = 2
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx
      .sharedData()
      .getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    launch {
      val messages = vertx
        .eventBus()
        .consumer<GameRoom>(ADDRESS)
        .toChannel(vertx)
      for (message in messages) {
        launch {
          val roomsName = message.headers()[ROOMS_NAME]
          val room = rooms.getAwait(roomsName)
          val newRoom = message.body()
          when {
            room == null -> message.reply(NO_ROOM_WITH_NAME)
            room.version == newRoom.version -> {
              // replaceIfPresentAwait is used to ensure that
              // no changes happened between the version check and the insertion
              val success = rooms.replaceIfPresentAwait(roomsName, room, newRoom.incrementVersion())
              message.reply(if (success) SUCCESS else ALREADY_CHANGED)
            }
            else -> message.reply(ALREADY_CHANGED)
          }
        }
      }
    }
    logger.info("Started a ${GameRoomUpdateVerticle::class.qualifiedName} instance")
  }
}
