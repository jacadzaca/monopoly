package com.jacadzaca.monopoly

import io.vertx.core.Promise
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.core.http.ServerWebSocket
import io.vertx.reactivex.ext.web.Router

class MainVerticle : AbstractVerticle() {
  private companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }
  override fun start(startPromise: Promise<Void>) {
    val router = Router.router(vertx)
    router.get("/").handler { request ->
      request
        .response()
        .rxSendFile("welcome.html")
        .subscribe()
    }
    val server = vertx.createHttpServer()

    val gameActionCodec = GameActionCodec()
    vertx
      .eventBus()
      .registerCodec(gameActionCodec)

    server
      .webSocketStream()
      .toFlowable()
      .subscribe(
        { handleWebSocketConnection(it, gameActionCodec) },
        { failStart(it, startPromise) })


    server
      .requestHandler(router)
      .rxListen(8080)
      .subscribe(
        { successfulStart(startPromise) },
        { failStart(it, startPromise) })
  }

  private fun handleWebSocketConnection(connection: ServerWebSocket, gameActionCodec: GameActionCodec) {
    connection
      .toFlowable()
      .map(Buffer::toJsonObject)
      .filter { GameAction.isValidJson(it) }
      .map(::GameAction)
      .subscribe(
        { handleIncomingAction(it, connection, gameActionCodec) },
        { handleWrongIncomingAction(it, connection) })
  }

  private fun handleWrongIncomingAction(error: Throwable, connection: ServerWebSocket) {
    logger.info("Invalid input to WebsSocket", error)
    connection.writeTextMessage("Invalid input")
  }

  private fun handleIncomingAction(action: GameAction, connection: ServerWebSocket, gameActionCodec: GameActionCodec) {
    vertx
      .eventBus()
      .rxRequest<String>(
        GameActionsVerticle.ADDRESS,
        action,
        deliveryOptionsOf(codecName = gameActionCodec.name(), headers = mapOf("room-id" to connection.path())))
      .map { it.body() }
      .doOnSuccess { connection.writeTextMessage(it) }
      .subscribe()
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
