package com.jacadzaca.monopoly.requests

import kotlinx.serialization.*

@Serializable
enum class PlayerAction {
  @SerialName("move")
  MOVE,
  @SerialName("buy-tile")
  BUY_TILE,
  @SerialName("buy-house")
  BUY_HOUSE,
  @SerialName("buy-hotel")
  BUY_HOTEL,
  @SerialName("join")
  JOIN,
  @SerialName("leave")
  LEAVE,
}
