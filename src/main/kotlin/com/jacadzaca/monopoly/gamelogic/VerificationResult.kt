package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.Player
import java.util.*

sealed class VerificationResult {
  data class VerifiedTilePurchaseEvent(
    val buyer: Player,
    val buyerId: UUID,
    val tile: Tile,
    val tileIndex: Int
  ) : VerificationResult(), GameStateChange

  data class VerifiedEstatePurchaseEvent(
    val buyer: Player,
    val buyerId: UUID,
    val tile: Tile,
    val tileIndex: Int,
    val estateType: EstateType
  ) : VerificationResult(), GameStateChange

  data class VerifiedMoveEvent(val mover: Player, val moverId: UUID): VerificationResult(),
    GameStateChange
  data class Failure(val reason: String) : VerificationResult()
}
