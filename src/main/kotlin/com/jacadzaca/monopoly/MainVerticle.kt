package com.jacadzaca.monopoly

import io.vertx.core.Promise
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.RxHelper
import io.vertx.reactivex.core.buffer.Buffer
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
    val server = vertx.createHttpServer()

    server
      .webSocketStream()
      .toFlowable()
      .doOnNext { connection ->
        connection
          .toFlowable()
          .map(Buffer::toJsonObject)
          .filter { GameAction.isValidJson(it) }
          .doOnNext { action ->
            vertx
              .eventBus()
              .rxRequest<String>(connection.path(), action)
              .map { it.body() }
              .doOnSuccess { connection.writeTextMessage(it) }
              .subscribe()
          }
          .subscribe()
      }
      .doOnError { error ->
        error.printStackTrace()
        startPromise.fail(error)
      }
      .subscribe()

    RxHelper
      .deployVerticle(vertx, GameActionsVerticle())
      .subscribe()

    server
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
