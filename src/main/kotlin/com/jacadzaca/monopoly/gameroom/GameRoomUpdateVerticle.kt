package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class GameRoomUpdateVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "update-game-room"
    internal val SUCCESS = Computation.success(Unit)
    internal val ALREADY_CHANGED =
      Computation.failure<Unit>("Changes were applied to this room during update request execution")
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val codec = deliveryOptionsOf(codecName = GenericCodec.computationCodecName(Unit::class))
    launch {
      for (message in vertx.eventBus().consumer<GameRoom>(ADDRESS).toChannel(vertx)) {
        launch {
          val roomsName = message.headers()[ROOMS_NAME]
          val result = GameRoomRepository
            .instance(vertx)
            .getById(roomsName)
            .map { room ->
              val newRoom = message.body()
              when (room.version) {
                newRoom.version -> {
                  // replaceIfPresentAwait is used to ensure that
                  // no changes have happened between the version check and the insertion
                  val success = rooms.replaceIfPresentAwait(roomsName, room, newRoom.incrementVersion())
                  if (success) SUCCESS else ALREADY_CHANGED
                }
                else -> ALREADY_CHANGED
              }
            }
          message.reply(result, codec)
        }
      }
    }
    logger.info("Started a ${GameRoomUpdateVerticle::class.qualifiedName} instance")
  }
}
