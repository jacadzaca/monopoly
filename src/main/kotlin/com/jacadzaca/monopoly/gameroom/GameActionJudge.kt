package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.Player

interface GameActionJudge {
  companion object {
    private val standardJudge = GameActionJudgeImpl()

    @JvmStatic
    fun create(): GameActionJudge {
      return standardJudge
    }
  }

  fun canPlayerExecuteAction(player: Player, action: GameAction): Boolean
}
