package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

/**
 * target.boardSize > PlayerMoves.newPosition >= 0
 */
data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  private val newPosition: Int,
  private val target: GameState,
  private val createPayment: (Player, UUID, Player, UUID, BigInteger, GameState) -> (LiabilityPayment)
) : Command() {
  override fun transform(): GameState {
    val tile = target.tiles[newPosition]
    return if (tile.ownersId != null && tile.ownersId != playersId) {
      createPayment(player, playersId, target.players[tile.ownersId]!!, tile.ownersId, tile.totalRent(), target).apply()
    } else {
      target
    }.update(playersId, player.setPosition(newPosition))
  }
}
