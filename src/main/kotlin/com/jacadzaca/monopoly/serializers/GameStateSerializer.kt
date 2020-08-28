package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

object GameStateSerializer : KSerializer<GameState> {
  private val playersMapSerializer = MapSerializer(UUIDSerializer, Player.serializer())
  private val tilesSerializer = ListSerializer(TileSerializer)
  private val eventsSerializer = ListSerializer(Event.serializer())

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(GameState::class.simpleName!!) {
    element("players", playersMapSerializer.descriptor)
    element("tiles", tilesSerializer.descriptor)
    element("events", eventsSerializer.descriptor)
  }

  /**
   * @throws IllegalAccessException if there are too many indexes
   */
  override fun deserialize(decoder: Decoder): GameState {
    return decoder.decodeStructure(descriptor) {
      val players: PersistentMap<UUID, Player>? = decodePlayers()
      val tiles: PersistentList<Tile>? = decodeTiles()
      val events: PersistentList<Event>? = decodeEvents()
      when {
        players == null -> throw SerializationException("Players fields was not specified when decoding a GameState, decoder=$decoder")
        tiles == null -> throw SerializationException("Tile fields was not specified when decoding a GameState, decoder=$decoder")
        events == null -> throw SerializationException("Events fields was not specified when decoding a GameState, decoder=$decoder")
        else -> GameState(players, tiles, events)
      }
    }
  }

  override fun serialize(encoder: Encoder, value: GameState) {
    encoder.encodeStructure(descriptor) {
      encodeSerializableElement(descriptor, 0, playersMapSerializer, value.players)
      encodeSerializableElement(descriptor, 1, tilesSerializer, value.tiles)
      encodeSerializableElement(descriptor, 2, eventsSerializer, value.events)
    }
  }

  private fun CompositeDecoder.decodePlayers(): PersistentMap<UUID, Player>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), playersMapSerializer).toPersistentMap()
  }

  private fun CompositeDecoder.decodeTiles(): PersistentList<Tile>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), tilesSerializer).toPersistentList()
  }

  private fun CompositeDecoder.decodeEvents(): PersistentList<Event>? {
    return decodeSerializableElement(descriptor, decodeElementIndex(descriptor), eventsSerializer).toPersistentList()
  }
}
