package com.jacadzaca.monopoly

import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import java.util.*

class PlayerInRedis(private val redisAPI: RedisAPI, private val id: UUID) : Player {
  private companion object {
    private const val UPDATED_EXISTING_KEY = 0
  }

  override fun updatePosition(newPosition: Int): Completable {
    return redisAPI
      .rxHset("${playerKey()} position $newPosition".split(' '))
      .flatMapCompletable { response ->
        when (response.toInteger()) {
          UPDATED_EXISTING_KEY -> Completable.complete()
          else -> Completable.error(RuntimeException("Redis had to create position key, which is not normal"))
        }
      }
  }

  override fun getPosition(): Single<Int> {
    return redisAPI
      .rxHget(playerKey(), "position")
      .map(Response::toInteger)
      .toSingle()
  }

  internal fun playerKey(): String {
    return "player:$id"
  }
}
