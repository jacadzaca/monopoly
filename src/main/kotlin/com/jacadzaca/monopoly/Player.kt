package com.jacadzaca.monopoly

import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.RedisAPI
import java.util.*

interface Player {
  val id: UUID
  companion object {
    fun playerInRedis(id: UUID, database: RedisAPI): Player = PlayerInRedis(id, database)
  }

  fun setPosition(newPosition: Int): Completable
  fun getPosition(): Single<Int>
}
