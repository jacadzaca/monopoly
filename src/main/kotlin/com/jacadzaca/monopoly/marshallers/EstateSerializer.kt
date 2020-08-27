package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

object EstateSerializer : KSerializer<Estate> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("estate") {
    element<Int>("type")
    element("rent", BigIntegerSerializer.descriptor)
    element("price", BigIntegerSerializer.descriptor)
  }

  override fun deserialize(decoder: Decoder): Estate {
    return decoder.decodeStructure(descriptor) {
      when (val type = decodeIntElement(descriptor, decodeElementIndex(descriptor))) {
        0 -> Estate.House(
          decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer),
          decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer)
        )
        1 -> Estate.Hotel(
          decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer),
          decodeSerializableElement(descriptor, decodeElementIndex(descriptor), BigIntegerSerializer)
        )
        else -> throw SerializationException("Unexpected id=$type")
      }
    }
  }

  override fun serialize(encoder: Encoder, value: Estate) {
    val type = when (value) {
      is Estate.House -> 0
      is Estate.Hotel -> 1
    }
    encoder.encodeStructure(descriptor) {
      encodeIntElement(descriptor, 0, type)
      encodeSerializableElement(descriptor, 1, BigIntegerSerializer, value.rent)
      encodeSerializableElement(descriptor, 2, BigIntegerSerializer, value.price)
    }
  }
}
