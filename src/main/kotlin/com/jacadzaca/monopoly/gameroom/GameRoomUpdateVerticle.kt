package com.jacadzaca.monopoly.gameroom

import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomUpdateVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "update-game-room"
    internal const val ROOMS_NAME = "roomsName"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val eventBus = vertx.eventBus()
      .registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
      .registerDefaultCodec(UpdateResult::class.java, UpdateResultCodec)
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    launch {
      val messages = eventBus.consumer<GameRoom>(ADDRESS).toChannel(vertx)
      for (message in messages) {
        val roomsName = message.headers()[ROOMS_NAME]
        vertx.sharedData().getLockAwait(roomsName).let { lock ->
          val room = rooms.getAwait(roomsName)
          val newRoom = message.body()
          when {
              room == null -> {
                  rooms.putAwait(roomsName, newRoom)
                  message.reply(UpdateResult.SUCCESS)
              }
              room.version == newRoom.version -> {
                  rooms.putAwait(roomsName, newRoom.incrementVersion())
                  message.reply(UpdateResult.SUCCESS)
              }
              else -> message.reply(UpdateResult.ALREADY_CHANGED)
          }
          lock.release()
        }
      }
    }
    logger.info("Started a ${GameRoomUpdateVerticle::class.qualifiedName} instance")
  }
}
