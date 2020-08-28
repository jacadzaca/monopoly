package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.math.*

object PlayerSerializer : KSerializer<Player> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("player") {
    element<Int>("position")
    element("balance", BigIntegerSerializer.descriptor)
  }

  override fun deserialize(decoder: Decoder): Player {
    return decoder.decodeStructure(descriptor) {
      var position: Int? = null
      var balance: BigInteger? = null
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> position = decodeIntElement(descriptor, index)
          1 -> balance = decodeSerializableElement(descriptor, index, BigIntegerSerializer)
          DECODE_DONE -> break
          else -> throw SerializationException("Unknown index=$index")
        }
      }
      if (position == null) {
        throw SerializationException("Position was not set while deserializing a player instance! decoder=$decoder")
      } else if (balance == null) {
        throw SerializationException("Balance was not set while deserializing a player instance! decoder=$decoder")
      }
      Player(position, balance)
    }
  }

  override fun serialize(encoder: Encoder, value: Player) {
    encoder.encodeStructure(descriptor) {
      encodeIntElement(descriptor, 0, value.position)
      encodeSerializableElement(descriptor, 1, BigIntegerSerializer, value.balance)
    }
  }
}
