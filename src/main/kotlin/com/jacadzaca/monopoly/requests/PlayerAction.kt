package com.jacadzaca.monopoly.requests

import kotlinx.serialization.Serializable

@Serializable
enum class PlayerAction {
  MOVE,
  BUY_TILE,
  BUY_HOUSE,
  BUY_HOTEL,
  JOIN,
  LEAVE,
}
