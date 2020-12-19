package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gameroom.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.requests.parsing.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.*
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.util.*

class WebSocketGameServer : CoroutineVerticle() {
  private companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val newPlayer = Player(balance = 1000.toBigInteger())
  }

  override suspend fun start() {
    val gameRoomRepository = GameRoomRepository.instance(vertx)
    val server = vertx.createHttpServer()
    launch {
      for (connection in server.websocketStream().toChannel(vertx)) {
        launch {
          val roomId = connection.path().drop(1)
          val playerId = UUID.randomUUID()
          gameRoomRepository
            .getById(roomId)
            .map { gameRoomRepository.update(roomId, it.updateGameState(it.gameState.put(playerId, newPlayer))) }
            .onFailure { error ->
              connection.writeTextMessage(error)
              connection.closeAwait()
            }
            .onSuccess {
              val adapter = vertx.receiveChannelHandler<String>()
              val requests = adapter.map { jsonToRequest(playerId, roomId, it) }
              connection.textMessageHandler(adapter::handle)
              while (adapter.isActive) {
                requests
                  .receive()
                  .onSuccess { connection.writeTextMessageAwait(it.gameState.toString()) }
                  .onFailure { connection.writeTextMessageAwait(it) }
              }
            }
        }
      }
    }
    vertx.deployVerticleAwait(GameRoomUpdateVerticle())
    server.listenAwait(8081)
    logger.info("Started a ${this::class.qualifiedName} instance")
  }

  private suspend fun jsonToRequest(playersId: UUID, roomsId: String, message: String): Computation<GameRoom> {
    val gameRoomRepository = GameRoomRepository.instance(vertx)
    val requestParser = JsonRequestParser(RequestFactoryImpl)
    return gameRoomRepository
      .getById(roomsId)
      .combine { requestParser.parse(message, playersId, it.gameState).map(Request::validate) }
      .map { (gameRoom, command) -> gameRoomRepository.update(roomsId, gameRoom.updateGameState(command.apply())) }
      .map { gameRoomRepository.getById(roomsId) }
  }
}
