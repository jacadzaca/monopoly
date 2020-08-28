package com.jacadzaca.monopoly.serializers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

object UUIDSerializer : KSerializer<UUID> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(UUID::class.simpleName!!, PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}
