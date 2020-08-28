package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

object PlayersMapSerializer : KSerializer<PersistentMap<UUID, Player>> {
  private val mapSerializer = MapSerializer(UUIDSerializer, Player.serializer())
  override val descriptor: SerialDescriptor = mapSerializer.descriptor

  override fun deserialize(decoder: Decoder): PersistentMap<UUID, Player> =
    decoder.decodeSerializableValue(mapSerializer).toPersistentMap()


  override fun serialize(encoder: Encoder, value: PersistentMap<UUID, Player>) =
    encoder.encodeSerializableValue(mapSerializer, value)
}
