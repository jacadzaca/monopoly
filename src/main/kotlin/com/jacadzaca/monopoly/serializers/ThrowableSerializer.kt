package com.jacadzaca.monopoly.serializers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object ThrowableSerializer : KSerializer<Throwable> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("error") {
    element("type", PrimitiveSerialDescriptor("type", PrimitiveKind.STRING))
    element("message", PrimitiveSerialDescriptor("message", PrimitiveKind.STRING))
  }

  override fun serialize(encoder: Encoder, value: Throwable) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, 0, "error")
      encodeStringElement(descriptor, 1, value.message!!)
    }
  }

  override fun deserialize(decoder: Decoder): Throwable {
    throw UnsupportedOperationException("Can only serialize")
  }
}
