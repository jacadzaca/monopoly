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
  private val room = mockk<GameRoom>()
  private val roomName = Random.nextString()

  @BeforeEach
  fun setUp(vertx: Vertx) {
    runBlocking {
      vertx.deployVerticleAwait(GameRoomLookupVerticle())
      val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
      rooms.putAwait(roomName, room)
    }
  }

  @Test
  // repeat in ordedr to check if the verticle responds to multiple requests
  @RepeatedTest(2)
  fun `verticle replies with the room if there is an entry under name it exists`(vertx: Vertx) {
    runBlocking {
      assertSame(room, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, roomName).body())
    }
  }

  @Test
  fun `verticle replies with null if there is no room under given name`(vertx: Vertx) {
    runBlocking {
      assertSame(null, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, "invalidName").body())
    }
  }
}
