package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.*
import io.vertx.core.http.*
import io.vertx.core.impl.logging.*
import io.vertx.ext.web.*
import io.vertx.kotlin.core.*
import io.vertx.kotlin.core.http.*
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.*

class HttpServer : CoroutineVerticle() {
  private companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override suspend fun start() {
    vertx
      .eventBus()
      .registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
      .registerDefaultCodec(Computation::class.java, ComputationCodec())
    vertx.deployVerticleAwait(GameRoomCreationVerticle())
    vertx.deployVerticleAwait(GameRoomLookupVerticle())
    vertx.deployVerticleAwait(GameRoomUpdateVerticle())
    vertx.deployVerticleAwait(WebSocketGameServer())

    val restApi = Router.router(vertx)
    restApi
      .post("/rooms/create")
      .handlerAwait { context ->
        context
          .request()
          .getMaybeParam("name")
          .map { GameRoomRepository.instance(vertx).createGameRoom(it) }
          .onSuccess { context.reroute(HttpMethod.GET, "/rooms/${context.request().getParam("name")}") }
          .onFailure { context.fail(400, Throwable(it)) }
      }
      .failureHandlerAwait { it.response().setStatusCode(it.statusCode()).endAwait(it.failure().message!!) }

    restApi
      .get("/rooms/:name")
      .handlerAwait { context ->
        context
          .request()
          .getMaybeParam("name")
          .onFailure { context.fail(400, Throwable(it)) }
          .map { GameRoomRepository.instance(vertx).getById(it) }
          .onSuccess { context.response().endAwait(it.gameState.toString()) }
          .onFailure { context.fail(404, Throwable(it)) }
      }
      .failureHandlerAwait { it.response().setStatusCode(it.statusCode()).endAwait(it.failure().message!!) }

    vertx
      .createHttpServer()
      .requestHandler(restApi::handle)
      .listenAwait(8080)
    logger.info("Started a ${this::class.qualifiedName} instance")
  }

  private fun HttpServerRequest.getMaybeParam(name: String): Computation<String> {
    val param: String? = getParam(name)
    return if (param == null) {
      Computation.failure("Missing $name param")
    } else {
      Computation.success(param)
    }
  }

  private fun Route.handlerAwait(requestHandler: suspend (RoutingContext) -> Unit): Route {
    return handler { launch { requestHandler(it) } }
  }

  private fun Route.failureHandlerAwait(failureHandler: suspend (RoutingContext) -> Unit): Route {
    return failureHandler { launch { failureHandler(it) } }
  }
}
