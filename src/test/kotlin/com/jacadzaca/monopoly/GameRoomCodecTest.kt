package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import io.vertx.core.buffer.*
import kotlinx.collections.immutable.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.random.Random

internal class GameRoomCodecTest {
  private val room = GameRoom(
    GameState(
      persistentMapOf(
        UUID.randomUUID() to Player(
          Random.nextPositive(),
          Random.nextPositive().toBigInteger()
        )
      ),
      persistentListOf(
        Tile(
          persistentListOf(Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
          persistentListOf(Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger())),
          Random.nextPositive().toBigInteger(),
          UUID.randomUUID()
        )
      ),
      persistentListOf(Event.PlayerMoved(UUID.randomUUID(), Random.nextPositive()))
    )
  )

  @Test
  fun `decode method is the inverse of the encode method`() {
    val buffer = Buffer.buffer()
    GameRoomCodec.encodeToWire(buffer, room)
    assertEquals(room, GameRoomCodec.decodeFromWire(0, buffer))
  }
}
