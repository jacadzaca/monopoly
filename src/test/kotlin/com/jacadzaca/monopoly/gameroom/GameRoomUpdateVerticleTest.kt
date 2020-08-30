package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomUpdateVerticle.Companion.INVALID_ROOM_ID
import com.jacadzaca.monopoly.gameroom.GameRoomUpdateVerticle.Companion.OTHER_CHANGE_WAS_APPLIED
import io.mockk.*
import io.vertx.core.*
import io.vertx.core.shareddata.*
import io.vertx.junit5.*
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
  private val roomsName = Random.nextString()
  private lateinit var rooms: AsyncMap<String, GameRoom>

  @BeforeEach
  fun setUp(vertx: Vertx) {
    every { room.version } returns Random.nextPositive().toLong()
    every { room.gameState } returns mockk()
    runBlocking {
      vertx.deployVerticle(GameRoomUpdateVerticle())
      rooms = vertx.sharedData().getLocalAsyncMapAwait("game-rooms")
      rooms.clearAwait()
      rooms.putAwait(roomsName, room)
    }
  }

  // repeat in order to check if the verticle dose not 'block' itself
  @RepeatedTest(5)
  fun `verticle updates the game room if the ids match and no changes were applied to the room`(vertx: Vertx) {
    val newRoom = mockk<GameRoom>()
    val newRoomWithIncrementedVersion = mockk<GameRoom>()
    val version = room.version
    every { newRoom.version } returns version
    every { newRoom.copy(version = version + 1) } returns newRoomWithIncrementedVersion
    runBlocking {
      val result = sendRequest(vertx, newRoom)
      assertEquals(UpdateResult.Success, result)
      assertSame(rooms.getAwait(roomsName), newRoomWithIncrementedVersion)
    }
  }

  @Test
  fun `verticle replies with Failure if user wants to update a room that was changed`(vertx: Vertx) {
    val changedRoom = mockk<GameRoom>()
    runBlocking {
      every { changedRoom.version } returns room.version + 1L
      assertEquals(OTHER_CHANGE_WAS_APPLIED, sendRequest(vertx, changedRoom))
      every { changedRoom.version } returns room.version + Random.nextPositive()
      assertEquals(OTHER_CHANGE_WAS_APPLIED, sendRequest(vertx, changedRoom))
    }
  }

  @Test
  fun `verticle replies with Failure if user passes an id that dose not match to any room`(vertx: Vertx) {
    runBlocking {
      assertEquals(INVALID_ROOM_ID, sendRequest(vertx, room, Random.nextString()))
    }
  }

  private suspend fun sendRequest(vertx: Vertx, room: GameRoom, roomsName: String = this.roomsName): UpdateResult {
    return vertx.eventBus()
      .requestAwait<UpdateResult>(
        GameRoomUpdateVerticle.ADDRESS,
        room,
        options = deliveryOptionsOf().addHeader(GameRoomUpdateVerticle.ROOMS_NAME, roomsName)
      ).body()
  }
}
