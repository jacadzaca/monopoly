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
  data class PlayerMoved(val playersId: UUID, val newPosition: Int) : Event()
  @Serializable
  data class TilePurchased(val buyersId: UUID, val purchasedTilesIndex: Int) : Event()
  @Serializable
  data class LiabilityPaid(val payersId: UUID, val receiversId: UUID, val liability: BigInteger) : Event()
  @Serializable
  data class EstatePurchased(val buyersId: UUID, val tileIndex: Int, val purchasedEstate: Estate) : Event()
}
