package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gameroom.codecs.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomUpdateVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "update-game-room"
    internal const val ROOMS_NAME = "roomsName"
    internal val INVALID_ROOM_ID = UpdateResult.Failure("No game room with such id")
    internal val OTHER_CHANGE_WAS_APPLIED = UpdateResult.Failure("Rooms' versions differ, cannot apply change")
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val eventBus = vertx.eventBus()
      .registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
      .registerDefaultCodec(UpdateResult.Failure::class.java, FailureResultCodec)
      .registerDefaultCodec(UpdateResult.Success::class.java, SuccessResultCodec)
    launch {
      val messages = eventBus.consumer<GameRoom>(ADDRESS).toChannel(vertx)
      for (message in messages) {
        val roomsName = message.headers()[ROOMS_NAME]
        vertx.sharedData().getLockAwait(roomsName).let { lock ->
          message.reply(updateGameRoom(roomsName, message.body()))
          lock.release()
        }
      }
    }
    logger.info("Started a ${GameRoomUpdateVerticle::class.qualifiedName} instance")
  }

  private suspend fun updateGameRoom(roomsName: String, updatedRoom: GameRoom): UpdateResult {
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val room = rooms.getAwait(roomsName)
    return when {
      room == null -> INVALID_ROOM_ID
      room.version != updatedRoom.version -> OTHER_CHANGE_WAS_APPLIED
      else -> {
        rooms.putAwait(roomsName, updatedRoom.incrementVersion())
        UpdateResult.Success
      }
    }
  }
}
