package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.GameRoomServiceImpl.Companion.invalidRoomId
import com.jacadzaca.monopoly.GameRoomServiceImpl.Companion.otherChangeWasApplied
import io.mockk.*
import io.vertx.core.*
import io.vertx.core.shareddata.*
import io.vertx.junit5.*
import io.vertx.kotlin.core.shareddata.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.*
import java.util.*
import kotlin.random.Random

@ExtendWith(VertxExtension::class)
internal class GameRoomServiceImplTest {
  private val room = mockk<GameRoom>(name = "room")
  private val roomsId = UUID.randomUUID()
  private lateinit var roomService: GameRoomService
  private lateinit var rooms: AsyncMap<UUID, GameRoom>
  private lateinit var lock: Lock

  @BeforeEach
  fun setUp(vertx: Vertx) {
    clearAllMocks()
    every { room.version } returns Random.nextPositive().toLong()
    runBlocking {
      rooms = vertx.sharedData().getLocalAsyncMapAwait("rooms")
      rooms.clearAwait()
      val getLock: suspend (UUID) -> (Lock) = { id ->
        lock = spyk(vertx.sharedData().getLockAwait(id.toString()))
        lock
      }
      roomService = GameRoomServiceImpl(rooms, getLock)
    }
  }

  @Test
  fun `updateGameState releases lock`() {
    runBlocking {
      roomService.updateGameState(UUID.randomUUID(), room)
      verify { lock.release() }

      every { room.copy(version = any()) } returns room
      rooms.putAwait(roomsId, room)
      roomService.updateGameState(roomsId, room)
      verify { lock.release() }

      val changedRoom = mockk<GameRoom>(relaxed = true)
      every { changedRoom.version } returns room.version + 1
      rooms.putAwait(roomsId, changedRoom)
      roomService.updateGameState(roomsId, room)
      verify { lock.release() }
    }
  }

  @Test
  fun `updateGameState updates the game room if the id matches to a room and no changes were applied to the room`() {
    val newRoom = mockk<GameRoom>(name = "new game room")
    val version = room.version
    every { newRoom.version } returns version
    every { newRoom.copy(version = version + 1) } returns newRoom
    runBlocking {
      rooms.put(roomsId, room)
      val result = roomService.updateGameState(roomsId, newRoom)
      assertEquals(UpdateResult.Success, result)
      assertEquals(rooms.getAwait(roomsId), newRoom)
    }
  }

  @Test
  fun `updateGameState returns Failure if user wants to update a room that was changed`() {
    val changedRoom = mockk<GameRoom>()
    val failure = UpdateResult.Failure(otherChangeWasApplied)
    runBlocking {
      rooms.putAwait(roomsId, changedRoom)
      every { changedRoom.version } returns room.version + 1L
      assertEquals(failure, roomService.updateGameState(roomsId, room))
      every { changedRoom.version } returns room.version + Random.nextPositive()
      assertEquals(failure, roomService.updateGameState(roomsId, room))
    }
  }

  @Test
  fun `updateGameState returns Failure if user passes an id that dose not match to any room`() {
    runBlocking {
      assertEquals(UpdateResult.Failure(invalidRoomId), roomService.updateGameState(roomsId, room))
    }
  }
}
