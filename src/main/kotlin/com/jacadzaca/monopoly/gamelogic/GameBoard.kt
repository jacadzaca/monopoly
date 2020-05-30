package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

interface GameBoard {
  fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean

  /**
   * pretends to roll a dice and then moves the @param player
   */
  fun movePlayer(player: Player): Player
  fun addFunds(player: Player, howMuch: BigInteger): Player
  fun detractFunds(player: Player, howMuch: BigInteger): Player
}
