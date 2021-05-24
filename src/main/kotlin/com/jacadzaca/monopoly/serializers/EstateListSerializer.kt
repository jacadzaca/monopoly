package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object EstateListSerializer : KSerializer<PersistentList<Estate>> {
  private val listSerializer = ListSerializer(Estate.serializer())

  override fun deserialize(decoder: Decoder): PersistentList<Estate> {
    return listSerializer.deserialize(decoder).toPersistentList()
  }

  override val descriptor: SerialDescriptor = listSerializer.descriptor

  override fun serialize(encoder: Encoder, value: PersistentList<Estate>) {
    listSerializer.serialize(encoder, value)
  }
}
