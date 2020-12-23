package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import com.jacadzaca.monopoly.requests.*
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
import java.util.*
import kotlin.random.Random

@ExtendWith(VertxExtension::class)
internal class RequestProcessingVerticleTest {
  private val room = mockk<GameRoom>()
  private val roomsId = Random.nextString()
  private val roomWithIncrementedVersion = mockk<GameRoom>()
  private lateinit var rooms: AsyncMap<String, GameRoom>

  companion object {
    private lateinit var vertx: Vertx
    private val request = mockk<Request>()
    private val factory = mockk<RequestValidatorFactory>()
    private val updateCommand = mockk<Command>()

    @BeforeAll
    @JvmStatic
    fun setUp() {
      runBlocking {
        vertx = Vertx.vertx()
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(Unit.serializer(), Unit::class))
        vertx.eventBus().registerCodec(GenericCodec.computationCodec(GameRoom.serializer(), GameRoom::class))
        vertx.eventBus().registerDefaultCodec(Request::class.java, GenericCodec(Request.serializer()))
        vertx.deployVerticleAwait(GameRoomLookupVerticle())

        every {
          factory.validatorFor(request).validate(any(), any())
        } returns Computation.success(updateCommand)
        vertx.deployVerticleAwait(RequestProcessingVerticle(factory))
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
  fun `verticle swaps game rooms' when their ids match`() {
    val updatedRoom = mockk<GameRoom>()
    every { room.updateGameState(updateCommand).incrementVersion() } returns updatedRoom
    runBlocking {
      rooms.putAwait(roomsId, room)
      updateRoom(vertx, request)
      assertEquals(updatedRoom, rooms.getAwait(roomsId))
    }
  }

  private suspend fun updateRoom(vertx: Vertx, request: Request): Computation<Unit> =
    vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        RequestProcessingVerticle.ADDRESS,
        request,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to roomsId, "requestersId" to UUID.randomUUID().toString()))
      )
      .body()
}
