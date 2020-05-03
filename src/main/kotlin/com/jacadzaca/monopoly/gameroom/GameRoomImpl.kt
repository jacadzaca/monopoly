package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.RedisAPI
import java.nio.charset.Charset
import java.util.*

class GameRoomImpl(private val database: RedisAPI, private val roomId: UUID) : GameRoom {
  override fun getCurrentPlayersId(): Single<UUID> {
    return database
      .rxLindex("$roomId:players", 0.toString())
      .map { UUID.fromString(it.toString(Charset.defaultCharset())) }
      .toSingle()
  }

  override fun publishAction(action: GameAction): Completable {
    return database
      .rxPublish(roomId.toString(), action.toString())
      .flatMapCompletable { Completable.complete() }
  }

  /**
   * TODO: what happens if roomID is invalid?
   */
  override fun listenToRoom(): Flowable<GameAction> {
    return database
      .rxSubscribe(listOf(roomId.toString()))
      .map { it.toList() }
      .map { responses -> responses.map { it.toString(Charset.defaultCharset()) } }
      .map { GameAction.creteFromList(it) }
      .toFlowable()
  }
}
