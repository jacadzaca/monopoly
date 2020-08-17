package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

sealed class Event {
  data class PlayerMoved(val playersId: UUID, val newPosition: Int): Event()
  data class TilePurchased(val buyersId: UUID, val purchasedTilesIndex: Int): Event()
  data class LiabilityPaid(val payersId: UUID, val receiversId: UUID, val liability: BigInteger): Event()
  data class EstatePurchased(val buyersId: UUID, val tileIndex: Int, val purchasedEstate: Estate): Event()
}
