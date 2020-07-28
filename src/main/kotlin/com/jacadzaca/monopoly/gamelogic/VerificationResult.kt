package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import java.util.*

sealed class VerificationResult {
  data class VerifiedEstatePurchaseEvent(
    val buyer: Player,
    val buyerId: UUID,
    val tile: Tile,
    val tileIndex: Int,
    val estateType: EstateType
  ) : VerificationResult()

  data class VerifiedMoveEvent(val mover: Player, val moverId: UUID): VerificationResult()
  data class Failure(val reason: String) : VerificationResult()
}
