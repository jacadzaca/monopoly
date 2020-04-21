package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.PlayerManagerImpl
import io.mockk.junit5.MockKExtension
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.RxHelper
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisAPI
import io.vertx.reactivex.redis.client.Response
import io.vertx.redis.client.impl.types.MultiType
import io.vertx.redis.client.impl.types.SimpleStringType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
class PlayerManagerImplTest {

  private val database: RedisAPI = RedisAPI.api(Redis.createClient(Vertx.vertx()))
  private val playerManager: PlayerManagerImpl = PlayerManagerImpl(database)

  @AfterEach
  fun cleanUp() {
    database.rxFlushall(emptyList())
  }

  @Test
  fun `savePlayer should execute onComplete if player was saved`(testContext: VertxTestContext) {
    val playerToBeSaved = Player()
    playerManager
      .savePlayer(playerToBeSaved)
      .subscribe(testContext::completeNow, testContext::failNow)
  }

  @Test
  fun `getPlayer should execute onSuccess if player with such id exists`(testContext: VertxTestContext) {
    val expectedPlayer = Player(piece = Piece((1)))
    database
      .rxHmset(expectedPlayer.redisHashDescription())
      .subscribe()
    playerManager
      .getPlayer(expectedPlayer.id)
      .doOnSuccess { player ->
        Assertions.assertEquals(expectedPlayer, player)
        testContext.completeNow()
      }
      .doOnComplete { testContext.failNow(IllegalStateException("Should have found the player")) }
      .doOnError(testContext::failNow)
      .subscribe()
  }

  @Test
  fun `getPlayer should execute onComplete if player dose not exist`(testContext: VertxTestContext) {
    val notExistingId = UUID.randomUUID()
    playerManager
      .getPlayer(notExistingId)
      .doOnSuccess { testContext.failNow(IllegalStateException("No value should have been returned")) }
      .doOnError(testContext::failNow)
      .doOnComplete {
        testContext.completeNow()
      }
      .subscribe()
  }
}

