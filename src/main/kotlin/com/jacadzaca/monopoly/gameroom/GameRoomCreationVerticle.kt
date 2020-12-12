package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.collections.immutable.*
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.*

class GameRoomCreationVerticle : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "create-game-room"
    internal const val ROOMS_NAME = "roomsName"
    internal val SUCCESS = Computation.success(Unit)
    internal val NAME_TAKEN = Computation.failure<Unit>("There already exists a room with such name")
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    val codec = deliveryOptionsOf(codecName = GenericCodec.computationCodecName(Unit::class))
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    launch {
      for (message in vertx.eventBus().consumer<String>(ADDRESS).toChannel(vertx)) {
        launch {
          val response = rooms.putIfAbsentAwait(message.body(), GameRoom.CLEAN_GAME_ROOM)
          message.reply(if (response == null) SUCCESS else NAME_TAKEN, codec)
        }
      }
    }
    logger.info("Started a ${this::class.qualifiedName} instance")
  }
}
