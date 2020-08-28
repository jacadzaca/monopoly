package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.serializers.UUIDSerializer.descriptor
import com.jacadzaca.monopoly.serializers.UUIDSerializer.deserialize
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

/**
 * DISCLAIMER:
 * DO NOT USE [kotlinx.serialization] IN ORDER DO DESERIALIZE UNTRUSTED INPUT!
 * [deserialize] methods throw Java's [IllegalStateException]
 * when the JSON string dose not comfort to the [descriptor] scheme
 */
object UUIDSerializer : KSerializer<UUID> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(UUID::class.simpleName!!, PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

  override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}
