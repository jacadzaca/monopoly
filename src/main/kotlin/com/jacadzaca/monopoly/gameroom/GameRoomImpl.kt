package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.GameActionCodec
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import java.nio.charset.Charset
import java.util.*

class GameRoomImpl(private val vertx: Vertx,
                   roomId: UUID)
  : GameRoom {
  private val database: RedisAPI = RedisAPI.api(Redis.createClient(vertx))
  private val roomInputAddress = "$roomId:input"
  private val playersListId = "$roomId:players"

  override fun getCurrentPlayersId(): Single<UUID> {
    return database
      .rxLindex(playersListId, 0.toString())
      .map { UUID.fromString(it.toString(Charset.defaultCharset())) }
      .toSingle()
  }

  override fun publishAction(action: GameAction): Completable {
    val gameActionCodecName = GameActionCodec().name()
    return Completable.fromAction {
      vertx
        .eventBus()
        .publish(roomInputAddress, action, deliveryOptionsOf(codecName = gameActionCodecName))
    }
  }

  override fun listenToRoom(): Flowable<GameAction> {
    return vertx
      .eventBus()
      .consumer<GameAction>(roomInputAddress)
      .toFlowable()
      .map { it.body() }
  }
}
