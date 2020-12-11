package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
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
  private val roomsId = Random.nextString()
  private lateinit var rooms: AsyncMap<String, GameRoom>

  @BeforeEach
  fun setUp(vertx: Vertx) {
    runBlocking {
      vertx.deployVerticleAwait(GameRoomCreationVerticle())
      rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
    }
    rooms.clear()
  }

  @Test
  fun `verticle creates a new game room under given id`(vertx: Vertx) {
    runBlocking {
      createRoom(vertx)
      assertEquals(GameRoom.CLEAN_GAME_ROOM, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle replies with success if room was created`(vertx: Vertx) {
    runBlocking {
      assertEquals(GameRoomCreationVerticle.SUCCESS, createRoom(vertx))
    }
  }

  @Test
  fun `verticle replies with NAME_TAKEN if a room with desired name already exists`(vertx: Vertx) {
    runBlocking {
      createRoom(vertx)
      assertEquals(GameRoomCreationVerticle.NAME_TAKEN, createRoom(vertx))
    }
  }

  private suspend fun createRoom(vertx: Vertx) =
    vertx.eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomCreationVerticle.ADDRESS,
        roomsId,
      )
      .body()
}
