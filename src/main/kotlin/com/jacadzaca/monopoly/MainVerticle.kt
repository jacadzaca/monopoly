package com.jacadzaca.monopoly

import io.vertx.core.Promise
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.core.http.ServerWebSocket

class MainVerticle : AbstractVerticle() {
  private companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun start(startPromise: Promise<Void>) {
    val server = vertx.createHttpServer()
    server
      .websocketStream()
      .toFlowable()
      .flatMap(ServerWebSocket::toFlowable)
      .map(Buffer::toJsonObject)

    server
      .rxListen(8080)
      .subscribe(
        { successfulStart(startPromise) },
        { failStart(it, startPromise) })
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
