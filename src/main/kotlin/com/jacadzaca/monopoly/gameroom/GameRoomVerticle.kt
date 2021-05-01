package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import io.vertx.core.impl.logging.*
import io.vertx.kotlin.core.eventbus.*

class GameRoomVerticle(private val roomsName: String, private var gameState: GameState, private val validator: ValidatorProxy) : AbstractVerticle() {
  companion object {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val codec = deliveryOptionsOf(codecName = GenericCodec.computationCodecName(Delta::class))
    private val gameStateCodec = deliveryOptionsOf(codecName = "gameStateCodec")
  }

  // TODO: add a lock to gameState accesses?
  override fun start(startPromise: Promise<Void>) {
    vertx
      .eventBus()
      .consumer<Request>(roomsName)
      .handler { message ->
        val request = message.body()
        validator
          .validate(request, gameState)
          .onSuccess { command ->
            gameState = if (request.changeTurn) {
              ChangeTurn(command.execute()).execute()
            } else {
              command.execute()
            }
            for (event in gameState.recentChanges) {
              vertx.eventBus().publish(roomsName + "INFO", event, codec)
            }
            gameState = gameState.clearRecentChanges()
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
