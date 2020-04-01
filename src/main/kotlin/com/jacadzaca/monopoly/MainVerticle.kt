package com.jacadzaca.monopoly

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {
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
