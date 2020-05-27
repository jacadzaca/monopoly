package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.GameAction
import io.vertx.core.Promise
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.core.http.ServerWebSocket

class MainVerticle : AbstractVerticle() {
  private companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }
  override fun start(startPromise: Promise<Void>) {
    val server = vertx.createHttpServer()

    val gameActionCodec = GameActionCodec()
    vertx
      .eventBus()
      .registerCodec(gameActionCodec)

    server
      .websocketStream()
      .toFlowable()
      .subscribe(
        { handleWebSocketConnection(it, gameActionCodec) },
        logger::error)
    server
      .rxListen(8080)
      .subscribe(
        { successfulStart(startPromise) },
        { failStart(it, startPromise) })
  }

  private fun handleWebSocketConnection(connection: ServerWebSocket, gameActionCodec: GameActionCodec) {
    val test = connection
      .toFlowable()
      .map(Buffer::toJsonObject)
      .filter { GameAction.isValidJson(it) }
      .map(::GameAction)
      .subscribe(
        { handleIncomingAction(it, connection, gameActionCodec) },
        { handleWrongIncomingAction(it, connection) })
    connection.endHandler { test.dispose() }
  }

  private fun handleWrongIncomingAction(error: Throwable, connection: ServerWebSocket) {
    logger.info("Invalid input to WebsSocket", error)
    connection.writeTextMessage("Invalid input")
  }

  private fun handleIncomingAction(action: GameAction, connection: ServerWebSocket, gameActionCodec: GameActionCodec) {
  }

  private fun successfulStart(startPromise: Promise<Void>) {
    println("Listening on port 8080")
    startPromise.complete()
  }

  private fun failStart(error: Throwable, startPromise: Promise<Void>) {
    logger.error("Failed to start MainVerticle", error)
    startPromise.fail(error)
  }
}
