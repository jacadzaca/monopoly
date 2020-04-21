package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.Player
import io.reactivex.Completable
import io.reactivex.Maybe
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import io.vertx.redis.client.impl.types.SimpleStringType
import java.nio.charset.Charset
import java.util.UUID

internal class PlayerManagerImpl(private val database: RedisAPI) : PlayerManager {
  companion object {
    private val OK = SimpleStringType.OK.toString()
  }

  override fun savePlayer(player: Player): Completable {
    return database
      .rxHmset(player.redisHashDescription())
      .flatMapCompletable { result ->
        when (result.toString()) {
          OK -> Completable.complete()
          else -> Completable.error(IllegalStateException("Redis responded with: $result"))
        }
      }
  }

  override fun getPlayer(id: UUID): Maybe<Player> {
    return database
      .rxHvals("player:${id.toString()}")
      .map(Response::toList)
      .map(this::toStringList)
      .filter { it.isNotEmpty() }
      .map { it.plus(id.toString()) }
      .map { Player.createFromList(it) }
  }

  private fun toStringList(list: List<Response>): List<String> {
    return list.map { it.toString(Charset.defaultCharset()) }
  }


}
