package com.jacadzaca.monopoly.gamelogic

interface GameBoard {
  companion object {
    val startTile = Tile(listOf(), 0.toBigInteger(), null)
  }
  fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean

  /**
   * pretends to roll a dice and then moves the @param player
   */
  fun movePlayer(player: Player): Player
  fun collectRent(from: Player): Player
}
