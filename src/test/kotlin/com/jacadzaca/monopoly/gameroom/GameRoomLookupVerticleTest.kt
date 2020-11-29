package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.mockk.*
import io.vertx.core.Vertx
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
internal class GameRoomLookupVerticleTest {
  private val room = mockk<GameRoom>()
  private val roomName = Random.nextString()
  private var isDeployed = false

  @BeforeEach
  fun setUp(vertx: Vertx) {
    if (!isDeployed) {
      runBlocking {
        vertx.deployVerticleAwait(GameRoomLookupVerticle())
        vertx.eventBus().registerDefaultCodec(Computation::class.java, ComputationCodec())
        val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
        rooms.putAwait(roomName, room)
      }
      isDeployed = true
    }
  }

  @Test
  // repeat in order to check if the verticle responds to multiple requests
  @RepeatedTest(2)
  fun `verticle replies with the room if there is an entry under name it exists`(vertx: Vertx) {
    runBlocking {
      assertSame(room, lookupRoom(vertx, roomName).value)
    }
  }

  @Test
  fun `verticle replies with null if there is no room under given name`(vertx: Vertx) {
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
