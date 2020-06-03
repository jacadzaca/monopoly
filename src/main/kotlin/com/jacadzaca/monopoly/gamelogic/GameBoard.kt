package com.jacadzaca.monopoly.gamelogic

interface GameBoard {
  companion object {
    val startTile = Tile(listOf(), 0.toBigInteger(), null)
  }
  fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean

  fun collectRent(from: Player): Player
}
