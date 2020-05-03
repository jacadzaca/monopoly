package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gameroom.GameRoomImpl
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import io.mockk.verify
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
import java.util.UUID
import java.util.concurrent.TimeUnit

@Extensions(
  ExtendWith(MockKExtension::class),
  ExtendWith(VertxExtension::class)
)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
class RoomMangerImplIntegrationTest {
  private val database = RedisAPI.api(Redis.createClient(Vertx.vertx()))
  private val roomId = UUID.randomUUID()
  private val roomManger = GameRoomImpl(database, roomId)

  @Test
  fun `isPlayerTurn should return true if it is the player's turn`(testContext: VertxTestContext) {
    val player = Player()
    makePlayersTurnCurrent(player)
    roomManger
      .isPlayersTurn(player.id)
      .doOnError(testContext::failNow)
      .doOnSuccess { testContext.completeNow() }
      .test()
      .await()
      .assertResult(true)
  }

  @Test
  fun `isPlayersTurn should return false if it is not the player's turn`(testContext: VertxTestContext) {
    val player = Player()
    val otherPlayer = Player()
    makePlayersTurnCurrent(otherPlayer)
    roomManger
      .isPlayersTurn(player.id)
      .doOnError(testContext::failNow)
      .doOnSuccess { testContext.completeNow() }
      .test()
      .await()
      .assertResult(false)
  }

  @Test
  fun `isPlayersTurn should return false if room dose not exist`(testContext: VertxTestContext) {
    val player = Player()
    roomManger
      .isPlayersTurn(player.id)
      .doOnError(testContext::failNow)
      .doOnSuccess { testContext.completeNow() }
      .test()
      .await()
      .assertResult(false)
  }

  private fun makePlayersTurnCurrent(player: Player) {
    database
      .rxLpush("$roomId:players ${player.id}".split(' '))
      .blockingGet()
  }

  @AfterEach
  private fun cleanUp() {
    database
      .rxFlushall(listOf())
      .blockingGet()
  }
}
