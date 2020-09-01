package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.mockk.*
import io.vertx.core.Vertx
import io.vertx.core.shareddata.*
import io.vertx.junit5.*
import io.vertx.kotlin.core.*
import io.vertx.kotlin.core.eventbus.*
import io.vertx.kotlin.core.shareddata.*
import kotlinx.coroutines.*
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
  private var isDeployed = false

  @BeforeEach
  fun setUp(vertx: Vertx) {
    every { room.version } returns Random.nextPositive().toLong()
    every { room.gameState } returns mockk()
    every { room.incrementVersion() } returns roomWithIncrementedVersion
    runBlocking {
      if (!isDeployed) {
        vertx.deployVerticleAwait(GameRoomUpdateVerticle())
        rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
        isDeployed = true
      }
      rooms.clearAwait()
    }
  }

  @Test
  fun `verticle saves the game room under given id`(vertx: Vertx) {
    runBlocking {
      assertSame(UpdateResult.SUCCESS, saveRoom(vertx, room))
      assertSame(room, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle updates the game room if their ids match and no changes were applied to the room`(vertx: Vertx) {
    runBlocking {
      rooms.putAwait(roomsId, room)
      assertSame(UpdateResult.SUCCESS, saveRoom(vertx, room))
      assertSame(roomWithIncrementedVersion, rooms.getAwait(roomsId))

      rooms.putAwait(roomsId, room)
      assertSame(UpdateResult.SUCCESS, saveRoom(vertx, room))
      assertSame(roomWithIncrementedVersion, rooms.getAwait(roomsId))
    }
  }

  @Test
  fun `verticle dose nothing if user wants to update a room that was changed`(vertx: Vertx) {
    val changedRoom = mockk<GameRoom>()
    every { changedRoom.incrementVersion() } returns mockk()
    runBlocking {
      rooms.putAwait(roomsId, room)
      every { changedRoom.version } returns room.version - 1L
      assertSame(UpdateResult.ALREADY_CHANGED, saveRoom(vertx, changedRoom))
      assertSame(room, rooms.getAwait(roomsId))

      rooms.putAwait(roomsId, room)
      every { changedRoom.version } returns room.version - Random.nextPositive()
      assertSame(UpdateResult.ALREADY_CHANGED, saveRoom(vertx, changedRoom))
      assertSame(room, rooms.getAwait(roomsId))
    }
  }

  private suspend fun saveRoom(vertx: Vertx, room: GameRoom) =
    vertx.eventBus()
      .requestAwait<UpdateResult>(
        GameRoomUpdateVerticle.ADDRESS,
        room,
        deliveryOptionsOf().addHeader(GameRoomUpdateVerticle.ROOMS_NAME, roomsId)
      ).body()
}
