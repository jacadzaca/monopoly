package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.DeltaCalculator

class PlayerDeltaCalculator(val boardSize: Int) : DeltaCalculator<Player> {
  override fun calculate(current: Player, previous: Player): PlayerDelta {
    TODO("Not yet implemented")
  }
}
