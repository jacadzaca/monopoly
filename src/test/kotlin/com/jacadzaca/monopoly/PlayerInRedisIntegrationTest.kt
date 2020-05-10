package com.jacadzaca.monopoly

import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
class PlayerInRedisIntegrationTest {
  private lateinit var player: PlayerInRedis
  private lateinit var database: RedisAPI
  private val playerId = UUID.randomUUID()

  @BeforeEach
  fun init(vertx: Vertx) {
    database = RedisAPI.api(Redis.createClient(vertx))
    player = PlayerInRedis(playerId, database)
  }

  @Test
  fun `updatePosition updates position key`(testContext: VertxTestContext) {
    addPlayer()
    val newPosition = 10
    player
      .setPosition(newPosition)
      .test()
      .await()
      .assertNoErrors()
      .assertComplete()

    val actualValue = database
      .rxHget(player.playerKey(), "position")
      .blockingGet()
      .toInteger()
    Assertions.assertEquals(newPosition, actualValue)
    testContext.completeNow()
  }

  @Test
  fun `updatePosition throws RuntimeException when Redis dose not respond with 0`() {
    val newPosition = 10
    player
      .setPosition(newPosition)
      .test()
      .await()
      .assertError(RuntimeException::class.java)
  }

  private fun addPlayer() {
    database
      .rxHmset("${player.playerKey()} position 1".split(' '))
      .blockingGet()
  }

  @AfterEach
  fun cleanUp() {
    database
      .rxFlushall(listOf())
      .blockingGet()
  }

}
