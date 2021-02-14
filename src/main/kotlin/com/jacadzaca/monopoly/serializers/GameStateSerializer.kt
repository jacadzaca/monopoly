package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
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

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(GameState::class.simpleName!!) {
    element("players", playersMapSerializer.descriptor)
    element("tiles", tilesSerializer.descriptor)
  }

  override fun deserialize(decoder: Decoder): GameState {
    return decoder.decodeStructure(descriptor) {
      val players: PersistentMap<UUID, Player>? = decodePlayers()
      val tiles: PersistentList<Tile>? = decodeTiles()
      when {
        players == null -> throw SerializationException("Players fields was not specified when decoding a GameState, decoder=$decoder")
        tiles == null -> throw SerializationException("Tile fields was not specified when decoding a GameState, decoder=$decoder")
        else -> GameState(players, tiles)
      }
    }
  }

  override fun serialize(encoder: Encoder, value: GameState) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, 0, playersMapSerializer, value.players)
      encodeSerializableElement(descriptor, 1, tilesSerializer, value.tiles)
    }
  }

  private fun CompositeDecoder.decodePlayers(): PersistentMap<UUID, Player>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), playersMapSerializer).toPersistentMap()
  }

  private fun CompositeDecoder.decodeTiles(): PersistentList<Tile>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), tilesSerializer).toPersistentList()
  }
}
