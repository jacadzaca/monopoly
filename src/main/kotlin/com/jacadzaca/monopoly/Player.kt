package com.jacadzaca.monopoly

import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.RedisAPI
import java.util.*

interface Player {
  companion object {
    fun playerInRedis(id: UUID, database: RedisAPI): Player = PlayerInRedis(database, id)
  }

  fun setPosition(newPosition: Int): Completable
  fun getId(): Single<UUID>
  fun getPosition(): Single<Int>
}
