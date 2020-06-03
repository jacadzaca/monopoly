package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.GameEvent
import com.jacadzaca.monopoly.GameActionCodec
import com.jacadzaca.monopoly.gamelogic.player.Player
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.eventbus.EventBus
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import java.util.UUID

class GameRoomImpl(private val eventBus: EventBus,
                   private val redis: RedisAPI,
                   private val playerManager: PlayerManager,
                   roomId: UUID)
  : GameRoom {
  internal val roomInputAddress = "$roomId:input"
  internal val playersListId = "$roomId:players"

  override fun getCurrentPlayer(): Single<Player> {
    return redis
      .rxLindex(playersListId, 0.toString())
      .map(Response::toString)
      .map(UUID::fromString)
      .flatMap(playerManager::getPlayer)
      .toSingle()
  }

  override fun publishAction(event: GameEvent): Completable {
    val gameActionCodecName = GameActionCodec().name()
    return Completable.fromAction {
      eventBus
        .publish(
          roomInputAddress,
          event,
          deliveryOptionsOf(codecName = gameActionCodecName))
    }
  }

  override fun listenToRoom(): Flowable<GameEvent> {
    return eventBus
      .consumer<GameEvent>(roomInputAddress)
      .toFlowable()
      .map { it.body() }
  }
}
