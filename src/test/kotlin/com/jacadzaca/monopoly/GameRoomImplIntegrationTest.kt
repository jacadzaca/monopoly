package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.GameRoomImpl
import io.mockk.junit5.MockKExtension
import io.reactivex.schedulers.Schedulers
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
import org.junit.jupiter.api.extension.Extensions
import java.util.*
import java.util.concurrent.TimeUnit

@Extensions(
  ExtendWith(MockKExtension::class),
  ExtendWith(VertxExtension::class)
)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
class GameRoomImplIntegrationTest {
  private lateinit var gameRoom: GameRoomImpl
  private lateinit var databse: RedisAPI
  private val roomId = UUID.randomUUID()

  @BeforeEach
  fun init(vertx: Vertx) {
    databse = RedisAPI.api(Redis.createClient(vertx))
    gameRoom = GameRoomImpl(vertx.eventBus(), databse, roomId)
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
    databse
      .rxLpush("${gameRoom.playersListId} $currentPlayersId".split(' '))
      .blockingGet()
  }

  @AfterEach
  fun cleanUp() {
    databse
      .rxFlushall(listOf())
      .blockingGet()
  }

}
