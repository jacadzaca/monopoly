package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.gameroom.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.validators.*
import com.jacadzaca.monopoly.serializers.*
import io.vertx.core.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.json.*
import java.util.*

class WebSocketGameServer : AbstractVerticle() {
  private companion object {
    private const val ROOMS_NAME = "test"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun start(startPromise: Promise<Void>) {
    val server = vertx.createHttpServer()
    vertx.eventBus().registerCodec(GenericCodec(Request.serializer(), "requestCodec"))
    vertx.eventBus().registerCodec(GenericCodec(GameStateSerializer, "gameStateCodec"))
    vertx.eventBus().registerCodec(GenericCodec.computationCodec(Unit.serializer(), Unit::class))
    vertx.eventBus().registerCodec(GenericCodec.computationCodec(Delta.serializer(), Delta::class))

    val gameRoomRepository = GameRoomRepositoryImpl(vertx)
    server.webSocketHandler { connection ->
      val playersId = UUID.randomUUID()
      gameRoomRepository
        .sendRequest(Request(PlayerAction.JoinAction, playersId, false), ROOMS_NAME)
        .flatMap { gameRoomRepository.getGameState(ROOMS_NAME) }
        .onSuccess { gameState ->
          val listener = gameRoomRepository
            .subscribe(ROOMS_NAME)
            .handler { connection.writeTextMessage(Json.encodeToString(it.body())) }
          connection.writeTextMessage(Json.encodeToString(GameStateSerializer, gameState))

          connection.textMessageHandler { message ->
            parseAction(message)
              .map { Computation.success(Request(it, playersId)) }
              .onSuccess { request ->
                gameRoomRepository
                  .sendRequest(request, ROOMS_NAME)
                  .onFailure { error ->
                    connection.writeTextMessage(Json.encodeToString(ThrowableSerializer, error))
                  }
              }
              .onFailure { errorMessage ->
                connection.writeTextMessage(Json.encodeToString(ThrowableSerializer, Throwable(errorMessage)))
              }
          }

          connection.endHandler {
            listener.unregister()
            gameRoomRepository.sendRequest(Request(PlayerAction.LeaveAction, playersId, false), ROOMS_NAME)
          }
        }
        .onFailure { error ->
          connection.writeTextMessage(Json.encodeToString(ThrowableSerializer, error))
          connection.end()
        }
    }

    vertx
      .deployVerticle(GameRoomVerticle(ROOMS_NAME, ValidatorProxyImpl))
      .compose { server.listen(8081) }
      .onSuccess {
        startPromise.complete()
        logger.info("Started a ${this::class.qualifiedName} instance")
      }
  }

  private fun parseAction(json: String): Computation<PlayerAction> {
    return try {
      when (val action = Json.decodeFromString<PlayerAction>(json)) {
        is PlayerAction.JoinAction -> throw SerializationException("Users cannot send a join requests")
        is PlayerAction.LeaveAction -> throw SerializationException("Users cannot send a leave requests")
        else -> Computation.success(action)
      }
    } catch (e: SerializationException) {
      Computation.failure("${e.message}")
    }
  }
}

