package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.util.*
import kotlin.reflect.*

object RequestSerializer : KSerializer<Request> {
  private val house = Estate.House(100.toBigInteger(), 1000.toBigInteger())
  private val hotel = Estate.Hotel(150.toBigInteger(), 1500.toBigInteger())
  private const val requiredHousesForHotel = 5

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("request") {
    element("type", PrimitiveSerialDescriptor("type", PrimitiveKind.STRING))
    element("playersId", UUIDSerializer.descriptor)
  }

  override fun deserialize(decoder: Decoder): Request {
    return decoder.decodeStructure(descriptor) {
      var type: String? = null
      var playersId: UUID? = null
      while (true) {
        when(val index = decodeElementIndex(descriptor)) {
          0 -> type = decodeStringElement(descriptor, index)
          1 -> playersId = decodeSerializableElement(descriptor, index, UUIDSerializer)
          DECODE_DONE -> break
          else -> throw SerializationException("No value for index=$index")
        }
      }
      if (type == null || playersId == null) {
        throw SerializationException("Missing required field")
      }
      when (type) {
        "move" -> PlayerMovementRequest(playersId, ::createMove)
        "buy-tile" -> TilePurchaseRequest(playersId, ::BuyTile)
        "buy-house" -> HousePurchaseRequest(playersId, house, ::BuyEstate)
        "buy-hotel" -> HotelPurchaseRequest(playersId, hotel, requiredHousesForHotel, ::BuyEstate)
        else -> throw SerializationException("Incorrect request type=$type")
      }
    }
  }

  private fun createMove(player: Player, playersId: UUID, context: GameState): MovePlayer {
    return MovePlayer(player, playersId, PositionCalculator.instance.calculate(player.position, context.tiles.size), context, ::PayLiability)
  }

  override fun serialize(encoder: Encoder, value: Request) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, 0, typeOf(value::class))
      encodeSerializableElement(descriptor, 1, UUIDSerializer, value.playersId())
    }
  }

  private fun <T : Request> typeOf(clazz: KClass<T>): String {
    return when(clazz) {
      PlayerMovementRequest::class -> "move"
      TilePurchaseRequest::class -> "buy-tile"
      HousePurchaseRequest::class -> "buy-house"
      HotelPurchaseRequest::class -> "buy-hotel"
      else -> throw SerializationException("Cannot serialize request of type: $clazz")
    }
  }
}
