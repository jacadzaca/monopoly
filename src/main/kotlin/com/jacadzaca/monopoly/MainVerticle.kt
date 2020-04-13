package com.jacadzaca.monopoly

import io.vertx.core.Promise
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val router = Router.router(vertx)
    router.get("/").handler { request ->
      request
        .response()
        .rxSendFile("welcome.html")
        .subscribe()
    }
    vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(8080)
      .doOnError {error ->
        error.printStackTrace()
        startPromise.fail(error)
      }
      .doOnSuccess {
        println("Listening on port 8080")
        startPromise.complete()
      }
      .subscribe()
  }

}
