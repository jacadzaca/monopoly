package com.jacadzaca.monopoly.marshallers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.math.*

object BigIntegerSerializer : KSerializer<BigInteger> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor(BigInteger::class.simpleName!!, PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): BigInteger = BigInteger(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: BigInteger) = encoder.encodeString(value.toString())
}
