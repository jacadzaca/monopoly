package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*
import kotlinx.collections.immutable.*

class GameRoomVerticle(private val roomsName: String) : AbstractVerticle() {
  companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val codec = deliveryOptionsOf(codecName = GenericCodec.computationCodecName(Event::class))
    private val tile = Tile(persistentListOf(), persistentListOf(), 1000.toBigInteger(), null)
    private val newGameState = GameState(persistentHashMapOf(), persistentListOf(tile, tile, tile, tile, tile))
    private val gameStateCodec = deliveryOptionsOf(codecName = "gameStateCodec")
  }

  // TODO: add lock to gameState accesses?
  override fun start(startPromise: Promise<Void>) {
    var gameState = newGameState
    vertx
      .eventBus()
      .consumer<Request>(roomsName)
      .handler { message ->
        val update = message.body()
        update
          .validate(gameState)
          .onSuccess { command ->
            gameState = command.apply()
            vertx.eventBus().publish(roomsName + "INFO", command.asEvent(), codec)
            message.reply(null)
          }
          .onFailure { error ->
            message.fail(1, error)
          }
      }
    vertx
      .eventBus()
      .consumer<Unit>("${roomsName}LOOKUP")
      .handler { it.reply(gameState, gameStateCodec) }
    logger.info("Started a ${this::class.qualifiedName} instance")
    startPromise.complete()
  }
}
