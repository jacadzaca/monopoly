package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Action
import com.jacadzaca.monopoly.gamelogic.Transformation
import java.util.*
import kotlin.random.Random

data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  private val rollDice: () -> Int
) : Action {
  companion object {
    private val roll6SidedDie = { Random.nextInt(1, 6) }
    fun create(player: Player, playersId: UUID): PlayerMoves {
      return PlayerMoves(player, playersId, roll6SidedDie)
    }
  }

  override fun apply(target: GameState): GameState {
    val diceRoll = rollDice()
    return target
      .update(playersId, player.copy(position = (player.position + diceRoll) % target.boardSize))
      .addTransformation(Transformation.PlayerMovement(playersId, diceRoll))
  }
}
