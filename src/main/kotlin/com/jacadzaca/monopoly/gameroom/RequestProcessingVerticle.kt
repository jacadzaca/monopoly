package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*
import java.util.*

class RequestProcessingVerticle(
  private val requestValidatorFactory: RequestValidatorFactory = RequestValidatorFactoryImpl,
) : CoroutineVerticle() {
  companion object {
    const val ADDRESS = "process-request-to-game-room"
    internal val ALREADY_CHANGED =
      Computation.failure<Unit>("Changes were applied to this room during update request execution")
    private val logger = LoggerFactory.getLogger(this::class.java)
    internal val SUCCESS = Computation.success(Unit)
  }

  override suspend fun start() {
    val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
    val codec = deliveryOptionsOf(codecName = GenericCodec.computationCodecName(Unit::class))
    launch {
      for (message in vertx.eventBus().consumer<Request>(ADDRESS).toChannel(vertx)) {
        launch {
          val request = message.body()
          val roomsName = message.headers()["roomsName"]
          val requestersId = UUID.fromString(message.headers()["requestersId"])
          val result = GameRoomRepository
            .instance(vertx)
            .getById(roomsName)
            .combine { requestValidatorFactory.validatorFor(request).validate(requestersId, it.gameState) }
            .map { (room, command) ->
              // race condition prevention
              val success = rooms.replaceIfPresentAwait(
                roomsName,
                room,
                room.updateGameState(command).incrementVersion()
              )
              if (success) SUCCESS else ALREADY_CHANGED
            }
          message.reply(result, codec)
        }
      }
    }
    logger.info("Started a ${RequestProcessingVerticle::class.qualifiedName} instance")
  }
}
