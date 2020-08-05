package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Transformation
import java.util.*

data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  private val rollDice: () -> Int
) : Transformation {
  override fun apply(target: GameState): GameState {
    val diceRoll = rollDice()
    return target
      .update(playersId, player.copy(position = (player.position + diceRoll) % target.boardSize))
      .addTransformation(this)
  }
}
