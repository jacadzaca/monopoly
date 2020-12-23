package com.jacadzaca.monopoly.requests

import kotlinx.serialization.Serializable

@Serializable
enum class Request {
  PLAYER_MOVE,
  TILE_PURCHASE,
  HOUSE_PURCHASE,
  HOTEL_PURCHASE
}
