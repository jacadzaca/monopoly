package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Liability
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.*

/**
 * target.boardSize > PlayerMoves.newPosition >= 0
 */
data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  val newPosition: Int,
  private val target: GameState,
  private val createPayment: (Player, UUID, Liability, GameState) -> (LiabilityPayment)
) : Transformation() {
  override fun transform(): GameState {
    val tile = target.tiles[newPosition]
    return if (tile.ownersId != null && tile.ownersId != playersId) {
      val liability = Liability(tile.totalRent(), target.players[tile.ownersId]!!, tile.ownersId)
      createPayment(player, playersId, liability, target).apply()
    } else {
      target
    }.update(playersId, player.setPosition(newPosition))
  }
}
