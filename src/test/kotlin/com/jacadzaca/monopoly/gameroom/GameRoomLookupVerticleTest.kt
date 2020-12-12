package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.mockk.*
import io.vertx.core.Vertx
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
internal class GameRoomLookupVerticleTest {
  companion object {
    private lateinit var vertx: Vertx
    private val room = mockk<GameRoom>()
    private val roomName = Random.nextString()

    @BeforeAll
    @JvmStatic
    fun setUp() {
      vertx = Vertx.vertx()
      runBlocking {
        vertx.deployVerticleAwait(GameRoomLookupVerticle())
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(GameRoom.serializer(), GameRoom::class))
        val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
        rooms.putAwait(roomName, room)
      }
    }
  }

  @Test
  // repeat in order to check if the verticle responds to multiple requests
  @RepeatedTest(2)
  fun `verticle replies with the room if there is an entry under name it exists`() {
    runBlocking {
      assertSame(room, lookupRoom(vertx, roomName).value)
    }
  }

  @Test
  fun `verticle replies with null if there is no room under given name`() {
    runBlocking {
      assertNotNull(lookupRoom(vertx, "invalidName").message)
    }
  }

  private suspend fun lookupRoom(vertx: Vertx, roomName: String): Computation<GameRoom> =
    vertx
      .eventBus()
      .requestAwait<Computation<GameRoom>>(GameRoomLookupVerticle.ADDRESS, roomName)
      .body()
}
