package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.Player
import io.reactivex.Completable
import io.reactivex.Maybe
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import io.vertx.redis.client.impl.types.SimpleStringType
import java.nio.charset.Charset
import java.util.UUID

internal class PlayerManagerRedis(private val database: RedisAPI) : PlayerManager {
  override fun savePlayer(player: Player): Completable {
    return database
      .rxHmset(player.redisHashDescription())
      .flatMapCompletable { Completable.complete() }
  }

  override fun getPlayer(id: UUID): Maybe<Player> {
    return database
      .rxHvals("player:$id")
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
