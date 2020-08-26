package com.jacadzaca.monopoly

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

@ExtendWith(VertxExtension::class)
internal class GameRoomLookupVerticleTest {
  private val room = mockk<GameRoom>(name = "room")
  private val room1 = mockk<GameRoom>(name = "room1")
  private val roomName = "test"
  private val roomName1 = "${roomName}1"

  @BeforeEach
  fun setUp(vertx: Vertx) {
    runBlocking {
      vertx.deployVerticleAwait(GameRoomLookupVerticle())
      val rooms = vertx.sharedData().getLocalAsyncMapAwait<String, GameRoom>("game-rooms")
      rooms.putAwait(roomName, room)
      rooms.putAwait(roomName1, room1)
    }
  }

  @Test
  fun `verticle replies with the room if there is an entry under name it exists`(vertx: Vertx) {
    runBlocking {
      assertEquals(room, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, roomName).body())
      assertEquals(room1, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, roomName1).body())
    }
  }

  @Test
  fun `verticle replies with null if there is no room under given name`(vertx: Vertx) {
    runBlocking {
      assertEquals(null, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, "invalidName").body())
      assertEquals(null, vertx.eventBus().requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, "invalidName1").body())
    }
  }
}
