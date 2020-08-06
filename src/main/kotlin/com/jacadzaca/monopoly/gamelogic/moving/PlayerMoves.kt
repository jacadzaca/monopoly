package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Transformation
import java.util.*

data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  val moveBy: Int,
  private val target: GameState
) : Transformation() {
  override fun transform(): GameState {
    return target
      .update(playersId, player.copy(position = (player.position + moveBy) % target.boardSize))
  }
}
