package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.GameActionCodec
import com.jacadzaca.monopoly.Player
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.reactivex.core.eventbus.EventBus
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import java.util.UUID

class GameRoomImpl(private val eventBus: EventBus,
                   private val database: RedisAPI,
                   private val playerManager: PlayerManager,
                   roomId: UUID)
  : GameRoom {
  internal val roomInputAddress = "$roomId:input"
  internal val playersListId = "$roomId:players"

  override fun getCurrentPlayer(): Single<Player> {
    return database
      .rxLindex(playersListId, 0.toString())
      .map(Response::toString)
      .map(UUID::fromString)
      .flatMap(playerManager::getPlayer)
      .toSingle()
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
