package com.jacadzaca.monopoly

import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import java.util.*

class PlayerInRedis(private val database: RedisAPI, private val id: UUID) : Player {
  private companion object {
    private const val UPDATED_EXISTING_KEY = 0
  }

  override fun setPosition(newPosition: Int): Completable {
    return database
      .rxHset("${playerKey()} position $newPosition".split(' '))
      .flatMapCompletable { response ->
        when (response.toInteger()) {
          UPDATED_EXISTING_KEY -> Completable.complete()
          else -> Completable.error(RuntimeException("Redis had to create position key, which is not normal"))
        }
      }
  }

  override fun getId(): Single<UUID> {
    return Single.just(id)
  }

  override fun getPosition(): Single<Int> {
    return database
      .rxHget(playerKey(), "position")
      .map(Response::toInteger)
      .toSingle()
  }

  internal fun playerKey(): String {
    return "player:$id"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PlayerInRedis

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
