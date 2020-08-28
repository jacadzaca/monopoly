package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object PersistentEstateListSerializer : KSerializer<PersistentList<Estate>> {
  private val estateListSerializer = ListSerializer(Estate.serializer())
  override val descriptor: SerialDescriptor = estateListSerializer.descriptor

  override fun deserialize(decoder: Decoder): PersistentList<Estate> {
    return decoder.decodeSerializableValue(estateListSerializer).toPersistentList()
  }

  override fun serialize(encoder: Encoder, value: PersistentList<Estate>) {
    encoder.encodeSerializableValue(estateListSerializer, value.toList())
  }
}
