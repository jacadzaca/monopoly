package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

interface GameBoard {
  fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean

  /**
   * pretends to roll a dice and then moves the @param player
   */
  fun movePlayer(player: Player): Player
  fun collectRent(from: Player): Player
  fun addFunds(to: Player, howMuch: BigInteger): Player
  fun detractFunds(from: Player, howMuch: BigInteger): Player
}
