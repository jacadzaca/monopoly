package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.PlayerManagerImpl
import io.mockk.junit5.MockKExtension
import io.reactivex.schedulers.Schedulers
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.TimeUnit

@Extensions(
  ExtendWith(MockKExtension::class),
  ExtendWith(VertxExtension::class)
)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
class PlayerManagerImplIntegrationTest {
  private val database: RedisAPI = RedisAPI.api(Redis.createClient(Vertx.vertx()))
  private val playerManager: PlayerManagerImpl = PlayerManagerImpl(database)

  @Test
  fun `savePlayer should complete when a player is saved`(testContext: VertxTestContext) {
    val playerToBeSaved = Player()
    playerManager
      .savePlayer(playerToBeSaved)
      .doOnComplete(testContext::completeNow)
      .doOnError { testContext.failNow(IllegalStateException("Test case should have completed")) }
      .test()
      .await()
      .assertComplete()
  }

  @Test
  fun `getPlayer should return the queried player`(testContext: VertxTestContext) {
    val expectedPlayer = Player()
    database
      .rxHmset(expectedPlayer.redisHashDescription())
      .blockingGet()

    playerManager
      .getPlayer(expectedPlayer.id)
      .doOnSuccess { testContext.completeNow() }
      .doOnComplete { testContext.failNow(IllegalStateException("Test case should have returned a Maybe with a value")) }
      .test()
      .await()
      .assertResult(expectedPlayer)

  }

  @Test
  fun `getPlayer should return an empty maybe when there is no player with given id`(testContext: VertxTestContext) {
    val notExistingId = UUID.randomUUID()
    playerManager
      .getPlayer(notExistingId)
      .doOnSuccess { testContext.failNow(IllegalStateException("Test case should have returned an empty Maybe")) }
      .doOnComplete { testContext.completeNow() }
      .test()
      .await()
      .assertNoValues()
  }

  @AfterEach
  fun cleanUp() {
    database
      .rxFlushall(listOf())
      .blockingGet()
  }
}
