package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.Vertx
import io.vertx.core.shareddata.*
import io.vertx.junit5.*
import io.vertx.kotlin.core.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import kotlin.random.*

@ExtendWith(VertxExtension::class)
internal class GameRoomCreationVerticleTest {
  private val roomsId = Random.nextString()

  companion object {
    private lateinit var rooms: AsyncMap<String, GameRoom>
    private lateinit var vertx: Vertx

    @BeforeAll
    @JvmStatic
    fun setUp() {
      vertx = Vertx.vertx()
      runBlocking {
        vertx.deployVerticleAwait(GameRoomCreationVerticle())
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(Unit.serializer(), Unit::class))
        rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
      }
    }
  }

  @BeforeEach
  fun cleanUp(vertx: Vertx) {
    runBlocking {
      rooms.clearAwait()
    }
  }

  @Test
  fun `verticle creates a new game room under given id`() {
    runBlocking {
      createRoom()
      assertEquals(GameRoom.CLEAN_GAME_ROOM, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle replies with success if room was created`() {
    runBlocking {
      assertEquals(GameRoomCreationVerticle.SUCCESS, createRoom())
    }
  }

  @Test
  fun `verticle replies with NAME_TAKEN if a room with desired name already exists`() {
    runBlocking {
      createRoom()
      assertEquals(GameRoomCreationVerticle.NAME_TAKEN, createRoom())
    }
  }

  private suspend fun createRoom() =
    vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomCreationVerticle.ADDRESS,
        roomsId,
      )
      .body()
}
