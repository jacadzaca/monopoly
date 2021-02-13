package com.jacadzaca.monopoly.requests

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

class RequestParser(val playersId: UUID) : KSerializer<Request> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("request") {
    element("type", PrimitiveSerialDescriptor("type", PrimitiveKind.STRING))
  }

  override fun deserialize(decoder: Decoder): Request {
    return decoder.decodeStructure(descriptor) {
      var type: String? = null
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> type = decodeStringElement(descriptor, index)
          CompositeDecoder.DECODE_DONE -> break
          else -> throw SerializationException("No value for index=$index")
        }
      }
      if (type == null) {
        throw SerializationException("Missing required field")
      }
      RequestSerializer.typeToRequest(type, playersId)
    }
  }

  override fun serialize(encoder: Encoder, value: Request) {
    throw UnsupportedOperationException("Can only deserialize")
  }
}
