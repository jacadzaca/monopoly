package com.jacadzaca.monopoly.gamelogic.moving

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Action
import java.util.*
import kotlin.random.Random

data class PlayerMoves(
  private val player: Player,
  private val playersId: UUID,
  private val rollDice: () -> Int
) : Action {
  override fun apply(target: GameState): GameState {
    return target
      .update(playersId, player.copy(position = (player.position + rollDice()) % target.boardSize))
  }
}
