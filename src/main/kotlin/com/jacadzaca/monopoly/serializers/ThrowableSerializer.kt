package com.jacadzaca.monopoly.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure

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
