package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.PlayerManagerImpl
import com.jacadzaca.monopoly.gameroom.GameRoomImpl
import io.reactivex.Completable
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import java.util.*

class GameActionsVerticle : AbstractVerticle() {
  companion object {
    const val ADDRESS = "game-actions"
    const val ROOM_ID = "roomId"
    private val logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun rxStart(): Completable {
    val redisConnection = RedisAPI.api(Redis.createClient(vertx))
    val playerManger = PlayerManagerImpl(redisConnection)
    vertx
      .eventBus()
      .consumer<GameAction>(ADDRESS) { message ->
        val action = message.body()
        val roomId = UUID.fromString(message.headers()[ROOM_ID])
        val gameRoom = GameRoomImpl(redisConnection, roomId)
        gameRoom
          .getCurrentPlayersId()
          .filter { it == action.committerId }
          .flatMap(playerManger::getPlayer)
          .map { updatePlayerPosition(it, action.moveSize) }
          .flatMapCompletable { playerManger.savePlayer(it) }
          .andThen(gameRoom.publishAction(action))
          .subscribe({}, logger::error)
      }
    return Completable.complete()
  }

  private fun updatePlayerPosition(player: Player, moveSize: Int): Player {
    return player.copy(piece = Piece(player.piece.position + moveSize))
  }
}
