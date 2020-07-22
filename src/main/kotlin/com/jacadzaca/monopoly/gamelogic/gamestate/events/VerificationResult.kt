package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile

sealed class VerificationResult {
  data class VerifiedTilePurchaseEvent(val player: Player, val tile: Tile) : VerificationResult()
  data class VerifiedEstatePurchaseEvent(val player: Player, val estateType: EstateType, val tile: Tile) :
    VerificationResult()

  data class Failure(val reason: String) : VerificationResult()
}
