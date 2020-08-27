package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.gamelogic.commands.*
import io.vertx.core.json.*
import java.math.*
import java.util.*

object JsonEventMarshaller {
  private const val id = "id"
  private const val newPosition = "new-position"
  private const val purchasedTileIndex = "purchased-tile-index"
  private const val payersId = "payers-${id}"
  private const val receiversId = "receiver-${id}"
  private const val liability = "liability"
  private const val estate = "estate"
  private const val tileIndex = "tile-index"
  private const val type = "type"

  fun encode(obj: Event): JsonObject {
    return when (obj) {
      is Event.PlayerMoved -> JsonObject()
        .put(id, obj.playersId.toString())
        .put(newPosition, obj.newPosition)
      is Event.TilePurchased -> JsonObject()
        .put(id, obj.buyersId.toString())
        .put(purchasedTileIndex, obj.purchasedTilesIndex)
      is Event.LiabilityPaid -> JsonObject()
        .put(payersId, obj.payersId.toString())
        .put(receiversId, obj.receiversId.toString())
        .put(liability, obj.liability.toString())
      is Event.EstatePurchased -> JsonObject()
        .put(id, obj.buyersId.toString())
        .put(tileIndex, obj.tileIndex)
        .put(estate, JsonEstateMarshaller.encode(obj.purchasedEstate))
    }.put(type, obj::class.simpleName)
  }

  fun decode(raw: JsonObject): Event {
    return when (raw.getString(type)) {
      Event.PlayerMoved::class.simpleName -> Event.PlayerMoved(
        UUID.fromString(raw.getString(id)),
        raw.getInteger(newPosition)
      )
      Event.TilePurchased::class.simpleName -> Event.TilePurchased(
        UUID.fromString(raw.getString(id)),
        raw.getInteger(purchasedTileIndex)
      )
      Event.LiabilityPaid::class.simpleName -> Event.LiabilityPaid(
        UUID.fromString(raw.getString(payersId)),
        UUID.fromString(raw.getString(receiversId)),
        BigInteger(raw.getString(liability))
      )
      Event.EstatePurchased::class.simpleName -> Event.EstatePurchased(
        UUID.fromString(raw.getString(id)),
        raw.getInteger(tileIndex),
        JsonEstateMarshaller.decode(raw.getJsonObject(estate)))
      else -> throw IllegalStateException("Cannot parse event of type ${raw.getString("type")}")
    }
  }
}
