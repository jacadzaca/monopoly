package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.math.*
import java.util.*

object TileSerializer : KSerializer<Tile> {
  private val estateListSerializer = ListSerializer(Estate.serializer())

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("tile") {
    element("houses", estateListSerializer.descriptor)
    element("hotels", estateListSerializer.descriptor)
    element("price", BigIntegerSerializer.descriptor)
    element("ownersId", UUIDSerializer.descriptor, isOptional = true)
  }

  /**
   * @throws IllegalMonitorStateException if there are too many indexes
   */
  override fun deserialize(decoder: Decoder): Tile {
    return decoder.decodeStructure(descriptor) {
      var houses = persistentListOf<Estate>()
      var hotels = persistentListOf<Estate>()
      var price = BigInteger.ZERO
      var ownersId: UUID? = null
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> houses = decodeEstateList(index).toPersistentList()
          1 -> hotels = decodeEstateList(index).toPersistentList()
          2 -> price = decodeSerializableElement(descriptor, index, BigIntegerSerializer)
          3 -> ownersId = decodeSerializableElement(descriptor, index, UUIDSerializer)
          DECODE_DONE -> break
          else -> throw SerializationException("No value for index=$index")
        }
      }
      Tile(houses, hotels, price, ownersId)
    }
  }

  override fun serialize(encoder: Encoder, value: Tile) {
    encoder.encodeStructure(descriptor) {
      encodeEstateList(0, value.houses.toList())
      encodeEstateList(1, value.hotels.toList())
      encodeSerializableElement(descriptor, 2, BigIntegerSerializer, value.price)
      if (value.ownersId != null) {
        encodeSerializableElement(descriptor, 3, UUIDSerializer, value.ownersId)
      }
    }
  }

  private fun CompositeEncoder.encodeEstateList(index: Int, value: List<Estate>) {
    encodeSerializableElement(descriptor, index, estateListSerializer, value)
  }

  private fun CompositeDecoder.decodeEstateList(index: Int): List<Estate> {
    return decodeSerializableElement(descriptor, index, estateListSerializer)
  }
}
