package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.GameActionCodec
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.eventbus.EventBus
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import java.nio.charset.Charset
import java.util.*

class GameRoomImpl(private val eventBus: EventBus,
                   private val database: RedisAPI,
                   roomId: UUID)
  : GameRoom {
  internal val roomInputAddress = "$roomId:input"
  internal val playersListId = "$roomId:players"

  override fun getCurrentPlayersId(): Maybe<UUID> {
    return database
      .rxLindex(playersListId, 0.toString())
      .map { UUID.fromString(it.toString()) }
  }

  override fun publishAction(action: GameAction): Completable {
    val gameActionCodecName = GameActionCodec().name()
    return Completable.fromAction {
      eventBus
        .publish(
          roomInputAddress,
          action,
          deliveryOptionsOf(codecName = gameActionCodecName))
    }
  }

  override fun listenToRoom(): Flowable<GameAction> {
    return eventBus
      .consumer<GameAction>(roomInputAddress)
      .toFlowable()
      .map { it.body() }
  }
}
