@file:UseSerializers(BigIntegerSerializer::class, UUIDSerializer::class)

package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import java.math.*
import java.util.*

@Serializable
sealed class Event {
  @Serializable
  @SerialName("moved")
  data class PlayerMoved(val playersId: UUID, val newPosition: Int) : Event()
  @Serializable
  @SerialName("tile-purchased")
  data class TilePurchased(val buyersId: UUID, val purchasedTilesIndex: Int) : Event()
  @Serializable
  @SerialName("liability-paid")
  data class LiabilityPaid(val payersId: UUID, val receiversId: UUID, val liability: BigInteger) : Event()
  @Serializable
  @SerialName("estate-purchased")
  data class EstatePurchased(val buyersId: UUID, val tileIndex: Int, val purchasedEstate: Estate) : Event()
  @Serializable
  @SerialName("player-joined")
  data class PlayerJoined(val playersId: UUID) : Event()
  @Serializable
  @SerialName("player-left")
  data class PlayerLeft(val playersId: UUID) : Event()
  @Serializable
  @SerialName("turn-change")
  object TurnChanged: Event()
}
