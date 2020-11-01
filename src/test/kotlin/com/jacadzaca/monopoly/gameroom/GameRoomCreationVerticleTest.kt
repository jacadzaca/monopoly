package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.mockk.*
import io.vertx.core.Vertx
import io.vertx.core.shareddata.*
import io.vertx.junit5.*
import io.vertx.junit5.Timeout
import io.vertx.kotlin.core.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import java.util.concurrent.*
import kotlin.random.*

@ExtendWith(VertxExtension::class)
@Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
internal class GameRoomCreationVerticleTest {
  private val room = mockk<GameRoom>()
  private val roomsId = Random.nextString()
  private lateinit var rooms: AsyncMap<String, GameRoom>
  private var isDeployed = false

  @BeforeEach
  fun setUp(vertx: Vertx) {
    every { room.version } returns Random.nextPositive().toLong()
    //every { room.gameState } returns mockk()
    if (!isDeployed) {
      runBlocking {
        vertx.deployVerticleAwait(GameRoomCreationVerticle())
        vertx.eventBus().registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
        rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
        isDeployed = true
      }
    }
    rooms.clear()
  }

  @Test
  fun `verticle saves the game room under given id`(vertx: Vertx) {
    runBlocking {
      saveRoom(vertx, room)
      assertSame(room, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle replies with success if room was saved`(vertx: Vertx) {
    runBlocking {
      assertEquals(GameRoomCreationVerticle.SUCCESS, saveRoom(vertx, room))
    }
  }

  @Test
  fun `verticle replies with failure if rooms with desired name already exists`(vertx: Vertx) {
    runBlocking {
      saveRoom(vertx, room)
      assertEquals(GameRoomCreationVerticle.NAME_TAKEN, saveRoom(vertx, room))
    }
  }

  private suspend fun saveRoom(vertx: Vertx, room: GameRoom) =
    vertx.eventBus()
      .requestAwait<Int>(
        GameRoomCreationVerticle.ADDRESS,
        room,
        deliveryOptionsOf(headers = mapOf(GameRoomCreationVerticle.ROOMS_NAME to roomsId))
      )
      .body()
}
