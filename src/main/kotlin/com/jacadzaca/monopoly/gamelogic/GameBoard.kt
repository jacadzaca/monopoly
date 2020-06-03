package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile

interface GameBoard {
  companion object {
    val startTile = Tile(listOf(), 0.toBigInteger(), null)
  }
  fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean

  fun collectRent(from: Player): Player
}
