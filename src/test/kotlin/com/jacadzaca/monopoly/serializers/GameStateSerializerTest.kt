package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.nextPositive
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.random.Random

internal class GameStateSerializerTest {
    private val gameState = GameState(
        persistentMapOf(
            UUID.randomUUID() to Player(
                Random.nextPositive(),
                Random.nextPositive().toBigInteger()
            )
        ),
        persistentListOf(
            Tile(
                persistentListOf(
                    Estate.House(
                        Random.nextPositive().toBigInteger(),
                        Random.nextPositive().toBigInteger()
                    )
                ),
                persistentListOf(
                    Estate.Hotel(
                        Random.nextPositive().toBigInteger(),
                        Random.nextPositive().toBigInteger()
                    )
                ),
                Random.nextPositive().toBigInteger(),
                UUID.randomUUID()
            )
        ),
    )

    @Test
    fun `encode method is the inverse of the decode method`() {
        assertEquals(
            gameState,
            Json.decodeFromString(GameStateSerializer, Json.encodeToString(GameStateSerializer, gameState))
        )
    }

    @Test
    fun `decode throws SerializationException if no players field is specified`() {
        assertThrows<SerializationException> {
            Json.decodeFromString(GameStateSerializer, "{\"tiles\": [], \"events\": []}")
        }
    }

    @Test
    fun `decode throws SerializationException if no tiles field is specified`() {
        assertThrows<SerializationException> {
            Json.decodeFromString(GameStateSerializer, "{\"players\": {}, \"events\": []}")
        }
    }
}
