package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import java.text.FieldPosition
import java.util.*

data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  val newPosition: Int,
  private val target: GameState
) : Transformation {
  override fun transform(): GameState {
    return target
      .update(playersId, player.setPosition(newPosition))
  }
}
