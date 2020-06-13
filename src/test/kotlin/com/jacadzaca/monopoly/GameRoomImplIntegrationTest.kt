package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.GameAction
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gameroom.GameRoomImpl
import com.jacadzaca.monopoly.gameroom.PlayerManager
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Maybe
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
internal class GameRoomImplIntegrationTest {
  private lateinit var gameRoom: GameRoomImpl
  private lateinit var redis: RedisAPI
  private lateinit var playerManager: PlayerManager
  private val roomId = UUID.randomUUID()

  @BeforeEach
  fun init(vertx: Vertx) {
    redis = RedisAPI.api(Redis.createClient(vertx))
    playerManager = mockk()
    gameRoom = GameRoomImpl(vertx.eventBus(), redis, playerManager ,roomId)
  }

  @Test
  fun `getCurrentPlayer should fetch the first element from room's players list`(testContext: VertxTestContext) {
    val currentPlayer = getTestPlayer()
    addPlayerToRoom(currentPlayer)

    gameRoom
      .getCurrentPlayer()
      .doOnSuccess { testContext.completeNow() }
      .doOnError { testContext.failNow(IllegalStateException("Test case should have returned currentPlayersId")) }
      .test()
      .await()
      .assertResult(currentPlayer)
  }

  @Test
  fun `publishAction should publish action to eventBus`(testContext: VertxTestContext, vertx: Vertx) {
    val action = getTestGameEvent()

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

  private fun addPlayerToRoom(player: Player) {
    redis
      .rxLpush("${gameRoom.playersListId} ${player.id}".split(' '))
      .blockingGet()
    every { playerManager.getPlayer(player.id) } returns Maybe.just(player)
  }

  @AfterEach
  fun cleanUp() {
    redis
      .rxFlushall(listOf())
      .blockingGet()
  }

}
