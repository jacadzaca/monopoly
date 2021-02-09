package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import java.util.*

object RequestSerializer : KSerializer<Request> {
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
        throw SerializationException("Missing required file")
      }
      when (type) {
        "move" -> PlayerMovementRequest(playersId, ::createMove)
        "buy-tile" -> TilePurchaseRequest(playersId, ::BuyTile)
        else -> throw SerializationException("Incorrect request type=$type")
      }
    }
  }

  private fun createMove(player: Player, playersId: UUID, context: GameState): MovePlayer {
    return MovePlayer(player, playersId, PositionCalculator.instance.calculate(player.position, context.tiles.size), context, ::PayLiability)
  }

  override fun serialize(encoder: Encoder, value: Request) {
    encoder.encodeStructure(descriptor) {
      when(value::class) {
        PlayerMovementRequest::class -> encodeStringElement(descriptor, 0, "move")
        TilePurchaseRequest::class -> encodeStringElement(descriptor, 0, "buy-tile")
        else -> throw SerializationException("Cannot serialize request of type: ${value::class}")
      }
      encodeSerializableElement(descriptor, 1, UUIDSerializer, value.playersId())
    }
  }
}
