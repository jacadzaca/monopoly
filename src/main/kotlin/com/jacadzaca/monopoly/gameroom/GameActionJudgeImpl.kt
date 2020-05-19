package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.Player

internal class GameActionJudgeImpl : GameActionJudge {
  override fun canPlayerExecuteAction(player: Player, action: GameAction): Boolean {
    return player.id == action.committerId
  }
}
