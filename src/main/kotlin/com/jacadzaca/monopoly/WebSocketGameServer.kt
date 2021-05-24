package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.Delta
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gameroom.GameRoomRepositoryImpl
import com.jacadzaca.monopoly.gameroom.GameRoomVerticle
import com.jacadzaca.monopoly.gameroom.GenericCodec
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.Request
import com.jacadzaca.monopoly.requests.ValidatorProxyImpl
import com.jacadzaca.monopoly.serializers.GameStateSerializer
import com.jacadzaca.monopoly.serializers.ThrowableSerializer
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.impl.logging.LoggerFactory
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigInteger
import java.util.UUID

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
                            .map { action ->
                                when (action) {
                                    is PlayerAction.NameChangeAction -> Computation.success(
                                        Request(
                                            action,
                                            playersId,
                                            changeTurn = false
                                        )
                                    )
                                    else -> Computation.success(Request(action, playersId))
                                }
                            }
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
            .deployVerticle(GameRoomVerticle(ROOMS_NAME, createGameState(), ValidatorProxyImpl))
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

    private fun createGameState(): GameState {
        val basePrice = 60.toBigInteger()
        val tiles = mutableListOf<Tile>()
        for (i in 1..22) {
            val tilesPrice = basePrice + ((i / 3) * 40).toBigInteger()
            tiles.add(Tile(persistentListOf(), persistentListOf(), tilesPrice, null, (tilesPrice / BigInteger.TEN)))
        }
        return GameState(persistentHashMapOf(), tiles.toPersistentList())
    }
}
