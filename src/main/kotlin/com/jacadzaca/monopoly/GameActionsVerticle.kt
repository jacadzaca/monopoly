package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.RoomManagerImpl
import io.reactivex.Completable
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import java.util.*

class GameActionsVerticle : AbstractVerticle() {
  companion object {
    const val ADDRESS = "game-actions"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun rxStart(): Completable {
    val roomManager = RoomManagerImpl(RedisAPI.api(Redis.createClient(vertx)))
    vertx
      .eventBus()
      .consumer<GameAction>(ADDRESS) { message ->
        val action = message.body()
        val roomId = UUID.fromString(message.headers()["roomId"])
        roomManager
          .isPlayersTurn(action.committerId, roomId)
          .subscribe({ playersTurn ->
            if (playersTurn) {
              roomManager.publishAction(roomId, action)
            }
          }, logger::error)
      }


    return Completable.complete()
  }
}
