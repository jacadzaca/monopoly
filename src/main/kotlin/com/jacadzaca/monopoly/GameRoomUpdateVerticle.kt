package com.jacadzaca.monopoly

import io.vertx.core.impl.logging.*
import io.vertx.core.shareddata.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

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
      val requests = eventBus
        .consumer<GameRoom>(ADDRESS)
        .toChannel(vertx)
        .map { it to it.headers()[ROOMS_NAME] }
      for ((message, roomsName) in requests) {
        val lock = vertx.sharedData().getLockAwait(roomsName)
        val updateResult = updateGameRoom(roomsName, message.body())
        message.reply(updateResult)
        lock.release()
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
        rooms.putAwait(roomsName, updatedRoom.copy(version = room.version + 1))
        UpdateResult.Success
      }
    }
  }
}
