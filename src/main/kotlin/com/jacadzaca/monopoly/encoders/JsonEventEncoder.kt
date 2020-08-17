package com.jacadzaca.monopoly.encoders

import com.jacadzaca.monopoly.gamelogic.commands.*
import io.vertx.core.json.*

object JsonEventEncoder : Encoder<Event, JsonObject> {
  override fun encode(obj: Event): JsonObject {
    return when (obj) {
      is Event.PlayerMoved -> JsonObject()
        .put("players-id", obj.playersId.toString())
        .put("new-position", obj.newPosition)
      is Event.TilePurchased -> JsonObject()
        .put("buyers-id", obj.buyersId.toString())
        .put("purchased-tile-index", obj.purchasedTilesIndex)
      is Event.LiabilityPaid -> JsonObject()
        .put("payers-id", obj.payersId.toString())
        .put("receivers-id", obj.receiversId.toString())
        .put("liability", obj.liability.toString())
      is Event.EstatePurchased -> JsonObject()
        .put("buyers-id", obj.buyersId.toString())
        .put("tile-index", obj.tileIndex)
        .put("estate-price", obj.purchasedEstate.price.toString())
        .put("estate-rent", obj.purchasedEstate.rent.toString())
    }
  }
}
