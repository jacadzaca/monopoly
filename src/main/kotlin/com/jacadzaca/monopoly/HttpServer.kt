package com.jacadzaca.monopoly

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.impl.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler

class HttpServer : AbstractVerticle() {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun start(startPromise: Promise<Void>) {
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route("/static/*").handler(StaticHandler.create("resources"))
        server.requestHandler(router::handle)
        server
            .listen(8080)
            .compose { vertx.deployVerticle(WebSocketGameServer()) }
            .onSuccess {
                startPromise.complete()
                logger.info("Started a ${this::class.qualifiedName} instance")
            }
    }
}
