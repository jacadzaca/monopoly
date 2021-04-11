package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.util.*

/**
 * DISCLAIMER:
 * DO NOT USE [kotlinx.serialization] IN ORDER DO DESERIALIZE UNTRUSTED INPUT!
 * [deserialize] methods throw Java's [IllegalStateException]
 * when the JSON string dose not comfort to the [descriptor] scheme
 */
object GameStateSerializer : KSerializer<GameState> {
  private val playersMapSerializer = MapSerializer(UUIDSerializer, Player.serializer())
  private val tilesSerializer = ListSerializer(TileSerializer)
  private val turnOrderSerializer = ListSerializer(UUIDSerializer)

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(GameState::class.simpleName!!) {
    element("players", playersMapSerializer.descriptor)
    element("tiles", tilesSerializer.descriptor)
    element("currentTurn", PrimitiveSerialDescriptor("currentTurn", PrimitiveKind.INT))
    element("turnOrder", turnOrderSerializer.descriptor)
  }

  override fun deserialize(decoder: Decoder): GameState {
    return decoder.decodeStructure(descriptor) {
      var players: PersistentMap<UUID, Player>? = null
      var tiles: PersistentList<Tile>? = null
      var currentTurn: Int? = null
      var turnOrder: PersistentList<UUID>? = null
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> players = decodeSerializableElement(descriptor, index, playersMapSerializer).toPersistentMap()
          1 -> tiles = decodeSerializableElement(descriptor, index, tilesSerializer).toPersistentList()
          2 -> currentTurn = decodeIntElement(descriptor, index)
          3 -> turnOrder = decodeSerializableElement(descriptor, index, turnOrderSerializer).toPersistentList()
          DECODE_DONE -> break
          else -> throw SerializationException("No value for index=$index")
        }
      }
      when {
        players == null -> throw SerializationException("Players field was not specified when decoding a GameState, decoder=$decoder")
        tiles == null -> throw SerializationException("Tile field was not specified when decoding a GameState, decoder=$decoder")
        currentTurn == null -> throw SerializationException("CurrentTurn field was not specified when decoding a GameState, decoder=$decoder")
        turnOrder == null -> throw SerializationException("TurnOrder field was not specified when decoding a GameState, decoder=$decoder")
        else -> GameState(players, tiles, currentTurn, turnOrder)
      }
    }
  }

  override fun serialize(encoder: Encoder, value: GameState) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, 0, playersMapSerializer, value.players)
      encodeSerializableElement(descriptor, 1, tilesSerializer, value.tiles)
      encodeIntElement(descriptor, 2, value.currentTurn)
      encodeSerializableElement(descriptor, 3, turnOrderSerializer, value.turnOrder)
    }
  }

  private fun CompositeDecoder.decodePlayers(): PersistentMap<UUID, Player>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), playersMapSerializer).toPersistentMap()
  }

  private fun CompositeDecoder.decodeTiles(): PersistentList<Tile>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), tilesSerializer).toPersistentList()
  }
}
