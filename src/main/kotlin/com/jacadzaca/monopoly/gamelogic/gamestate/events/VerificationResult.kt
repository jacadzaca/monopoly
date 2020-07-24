package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameStateChange
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.tiles.Tile

sealed class VerificationResult {
  data class VerifiedTilePurchaseEvent(
    val player: Player,
    val playerId: PlayerID,
    val tile: Tile,
    val tileIndex: Int
  ) : VerificationResult(), GameStateChange

  data class VerifiedEstatePurchaseEvent(
    val player: Player,
    val playerId: PlayerID,
    val tile: Tile,
    val tileIndex: Int,
    val estateType: EstateType
  ) : VerificationResult(), GameStateChange

  data class VerifiedMoveEvent(val player: Player, val playerId: PlayerID): VerificationResult(), GameStateChange
  data class Failure(val reason: String) : VerificationResult()
}
