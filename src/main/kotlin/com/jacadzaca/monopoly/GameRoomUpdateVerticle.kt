package com.jacadzaca.monopoly

import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomUpdateVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "update-game-room"
    internal const val ROOMS_NAME = "roomsName"
    internal const val INVALID_ROOM_ID = "No game room with such id"
    internal const val OTHER_CHANGE_WAS_APPLIED = "Rooms' versions differ, cannot apply change"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val eventBus = vertx.eventBus()
      .registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
      .registerDefaultCodec(UpdateResult.Failure::class.java, FailureResultCodec)
      .registerDefaultCodec(UpdateResult.Success::class.java, SuccessResultCodec)
    launch {
      val requests = eventBus.consumer<GameRoom>(ADDRESS).toChannel(vertx)
      for (request in requests) {
        val roomsName = request.headers()[ROOMS_NAME]
        val updateWith = request.body()
        //val lock = vertx.sharedData().getLockAwait(roomsName)
        val room = rooms.getAwait(roomsName)
        val result = when {
          room == null -> UpdateResult.Failure(INVALID_ROOM_ID)
          room.version != updateWith.version -> UpdateResult.Failure(OTHER_CHANGE_WAS_APPLIED)
          else -> {
            rooms.putAwait(roomsName, request.body().copy(version = room.version + 1))
            UpdateResult.Success
          }
        }
        //lock.release()
        request.reply(result)
      }
    }
    logger.info("Started a ${GameRoomUpdateVerticle::class.qualifiedName} instance")
  }
}
