package com.jacadzaca.monopoly.requests

import kotlinx.serialization.*

@Serializable
sealed class PlayerAction {
  @Serializable
  @SerialName("move")
  object MoveAction: PlayerAction()
  @Serializable
  @SerialName("buy-tile")
  object BuyTileAction: PlayerAction()
  @Serializable
  @SerialName("buy-house")
  object BuyHouseAction: PlayerAction()
  @Serializable
  @SerialName("buy-hotel")
  object BuyHotelAction: PlayerAction()
  @Serializable
  @SerialName("join")
  object JoinAction: PlayerAction()
  @Serializable
  @SerialName("leave")
  object LeaveAction: PlayerAction()
  @Serializable
  @SerialName("change-name")
  data class NameChangeAction(val newName: String): PlayerAction()
}

