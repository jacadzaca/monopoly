package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
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
internal class GameRoomUpdateVerticleTest {
  private val room = mockk<GameRoom>()
  private val roomsId = Random.nextString()
  private val roomWithIncrementedVersion = mockk<GameRoom>()
  private lateinit var rooms: AsyncMap<String, GameRoom>
  private var isDeployed = false

  @BeforeEach
  fun setUp(vertx: Vertx) {
    every { room.version } returns Random.nextPositive().toLong()
    every { room.gameState } returns mockk()
    every { room.incrementVersion() } returns roomWithIncrementedVersion
    runBlocking {
      if (!isDeployed) {
        vertx.deployVerticleAwait(GameRoomUpdateVerticle())
        vertx.eventBus().registerDefaultCodec(GameRoom::class.java, GameRoomCodec)
        vertx.eventBus().registerDefaultCodec(ComputationResult::class.java, ComputationCodec())
        rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
        isDeployed = true
      }
      rooms.clearAwait()
    }
  }

  @Test
  fun `verticle swaps game rooms' if their ids match`(vertx: Vertx) {
    runBlocking {
      rooms.putAwait(roomsId, room)
      assertEquals(GameRoomUpdateVerticle.SUCCESS, saveRoom(vertx, room))
      assertSame(roomWithIncrementedVersion, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle replies with failure when no game room with specified id exist`(vertx: Vertx) {
    runBlocking {
      assertEquals(GameRoomUpdateVerticle.NO_ROOM_WITH_NAME, saveRoom(vertx, room))
    }
  }

  @Test
  fun `verticle dose nothing if user wants to update a room that was changed`(vertx: Vertx) {
    val changedRoom = mockk<GameRoom>()
    every { changedRoom.incrementVersion() } returns mockk()
    runBlocking {
      rooms.putAwait(roomsId, room)
      every { changedRoom.version } returns room.version - 1L
      assertEquals(GameRoomUpdateVerticle.ALREADY_CHANGED, saveRoom(vertx, changedRoom))
      assertSame(room, rooms.getAwait(roomsId))

      rooms.putAwait(roomsId, room)
      every { changedRoom.version } returns room.version - Random.nextPositive()
      assertEquals(GameRoomUpdateVerticle.ALREADY_CHANGED, saveRoom(vertx, changedRoom))
      assertSame(room, rooms.getAwait(roomsId))
    }
  }

  private suspend fun saveRoom(vertx: Vertx, room: GameRoom) =
    vertx.eventBus()
      .requestAwait<Int>(
        GameRoomUpdateVerticle.ADDRESS,
        room,
        deliveryOptionsOf()
          .addHeader(ROOMS_NAME, roomsId)
          .setCodecName(GameRoomCodec.name())
      ).body()
}
