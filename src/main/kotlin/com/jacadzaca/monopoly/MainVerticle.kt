package com.jacadzaca.monopoly

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.ServerWebSocket
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {
  private val rooms: Map<String, GameManager> = mapOf<String, GameManager>("jaca" to GameMangerImpl())

  private fun handle(context: ServerWebSocket) {
    val roomName: String = context.path().substring(1)
    val gameRoom: GameManager? = rooms[roomName]
    if (gameRoom == null) {
      context.writeFinalTextFrame("Not such room").reject(404)
    }
    gameRoom!!.newPlayer("guest", context)
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
    router.route("/room/:roomName").handler { request ->
      val response = rooms[request.request().getParam("roomName")]?.players()?.toString() ?: "No such room"
      request
        .response()
        .putHeader("content-type", "text/plain")
        .end(response)
    }
    vertx
      .createHttpServer()
      .webSocketHandler(::handle)
      .requestHandler(router)
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
