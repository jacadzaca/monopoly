package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.mockk.*
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
internal class GameRoomUpdateVerticleTest {
  private val room = mockk<GameRoom>()
  private val roomsId = Random.nextString()
  private val roomWithIncrementedVersion = mockk<GameRoom>()
  private lateinit var rooms: AsyncMap<String, GameRoom>

  private companion object {
    private lateinit var vertx: Vertx

    @BeforeAll
    @JvmStatic
    fun setUp() {
      runBlocking {
        vertx = Vertx.vertx()
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(Unit.serializer(), Unit::class))
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(GameRoom.serializer(), GameRoom::class))
        vertx.eventBus().registerDefaultCodec(GameRoom::class.java, GenericCodec(GameRoom.serializer()))
        vertx.deployVerticleAwait(GameRoomUpdateVerticle())
        vertx.deployVerticleAwait(GameRoomLookupVerticle())
      }
    }
  }

  @BeforeEach
  fun cleanUp() {
    every { room.version } returns Random.nextPositive().toLong()
    every { room.gameState } returns mockk()
    every { room.incrementVersion() } returns roomWithIncrementedVersion
    runBlocking {
      rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
      rooms.clearAwait()
    }
  }

  @Test
  fun `verticle swaps game rooms' if their ids match`() {
    runBlocking {
      rooms.putAwait(roomsId, room)
      assertEquals(GameRoomUpdateVerticle.SUCCESS, saveRoom(vertx, room))
      assertSame(roomWithIncrementedVersion, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle passes down failure when no game room with specified id exist`() {
    runBlocking {
      assertNull(saveRoom(vertx, room).value)
      assertNotNull(saveRoom(vertx, room).message)
    }
  }

  @Test
  fun `verticle dose nothing if user wants to update a room that was changed`() {
    val changedRoom = mockk<GameRoom>()
    every { changedRoom.incrementVersion() } returns mockk()
    runBlocking {
      rooms.putAwait(roomsId, room)
      every { changedRoom.version } returns room.version - Random.nextPositive()
      assertEquals(GameRoomUpdateVerticle.ALREADY_CHANGED, saveRoom(vertx, changedRoom))
      assertSame(room, rooms.getAwait(roomsId))
    }
  }

  private suspend fun saveRoom(vertx: Vertx, room: GameRoom): Computation<Unit> =
    vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomUpdateVerticle.ADDRESS,
        room,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to roomsId))
      )
      .body()
}
