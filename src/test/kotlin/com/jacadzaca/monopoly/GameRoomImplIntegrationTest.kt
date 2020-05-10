package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.GameRoomImpl
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
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
internal class GameRoomImplIntegrationTest {
  private lateinit var gameRoom: GameRoomImpl
  private lateinit var database: RedisAPI
  private val roomId = UUID.randomUUID()

  @BeforeEach
  fun init(vertx: Vertx) {
    database = RedisAPI.api(Redis.createClient(vertx))
    gameRoom = GameRoomImpl(vertx.eventBus(), database, roomId)
  }

  @Test
  fun `getCurrentPlayersId should fetch the first index from redis list`(testContext: VertxTestContext) {
    val currentPlayersId = UUID.randomUUID()
    addPlayerToRoom(currentPlayersId)

    gameRoom
      .getCurrentPlayersId()
      .doOnSuccess { testContext.completeNow() }
      .doOnError { testContext.failNow(IllegalStateException("Test case should have returned currentPlayersId")) }
      .test()
      .await()
      .assertResult(currentPlayersId)
  }

  @Test
  fun `getCurrentPlayerId should return empty Maybe if there is no current player`(testContext: VertxTestContext) {
    gameRoom
      .getCurrentPlayersId()
      .doOnSuccess { testContext.failNow(IllegalStateException("No element was supposed to be found")) }
      .doOnComplete { testContext.completeNow() }
      .test()
      .await()
      .assertNoValues()
  }

  @Test
  fun `publishAction should publish action to eventBus`(testContext: VertxTestContext, vertx: Vertx) {
    val action = GameAction(UUID.randomUUID(), 1)

    vertx
      .eventBus()
      .registerCodec(GameActionCodec())
      .consumer<GameAction>(gameRoom.roomInputAddress) { message ->
        Assertions.assertEquals(action, message.body())
        testContext.completeNow()
      }

    gameRoom
      .publishAction(action)
      .subscribe()
  }

  private fun addPlayerToRoom(currentPlayersId: UUID) {
    database
      .rxLpush("${gameRoom.playersListId} $currentPlayersId".split(' '))
      .blockingGet()
  }

  @AfterEach
  fun cleanUp() {
    database
      .rxFlushall(listOf())
      .blockingGet()
  }

}
