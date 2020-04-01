package com.jacadzaca.monopoly

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.ServerWebSocket
import io.vertx.ext.web.Router
import java.util.LinkedList

class MainVerticle : AbstractVerticle() {
  private val gameRoom = GameRoomImpl(LinkedList(listOf(NetworkPlayer(null, Piece()))), MonopolyLogicImpl())

  private fun handle(connection: ServerWebSocket) {
    connection.textMessageHandler {
      connection.writeTextMessage(gameRoom.executeAction(it))
    }
  }

  override fun start(startPromise: Promise<Void>) {
    val router : Router = Router.router(vertx)
    router.route("/").handler { request ->
      request
        .response()
        .sendFile("welcome.html")
        .end()
    }
    router.route("/websocket.js").handler { request ->
      request
        .response()
        .sendFile("websocket.js")
        .end()
    }
    vertx
      .createHttpServer()
      .requestHandler(router)
      .webSocketHandler(::handle)
      .listen(8080) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8080")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}
