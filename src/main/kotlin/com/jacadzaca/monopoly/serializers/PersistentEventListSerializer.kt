package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.commands.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object PersistentEventListSerializer : KSerializer<PersistentList<Event>> {
  private val listSerializer = ListSerializer(Event.serializer())

  override val descriptor: SerialDescriptor = listSerializer.descriptor

  override fun deserialize(decoder: Decoder): PersistentList<Event> =
    decoder.decodeSerializableValue(listSerializer).toPersistentList()

  override fun serialize(encoder: Encoder, value: PersistentList<Event>) =
    encoder.encodeSerializableValue(listSerializer, value)
}
