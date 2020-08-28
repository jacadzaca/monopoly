package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.serializers.BigIntegerSerializer.descriptor
import com.jacadzaca.monopoly.serializers.BigIntegerSerializer.deserialize
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.math.*

/**
 * DISCLAIMER:
 * DO NOT USE [kotlinx.serialization] IN ORDER DO DESERIALIZE UNTRUSTED INPUT!
 * [deserialize] methods throw Java's [IllegalStateException]
 * when the JSON string dose not comfort to the [descriptor] scheme
 */
object BigIntegerSerializer : KSerializer<BigInteger> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor(BigInteger::class.simpleName!!, PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): BigInteger = BigInteger(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: BigInteger) = encoder.encodeString(value.toString())
}
