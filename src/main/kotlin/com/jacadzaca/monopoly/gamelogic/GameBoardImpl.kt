package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int,
                             private val tiles: List<Tile>)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean {
    return player.id == event.committerId
  }

  override fun collectRent(from: Player): Player {
    val tile = from.position
    if (tile.owner != null && tile.owner != from.id) {
      return from.copy(
        liability = Liability(tile.totalRent(), tile.owner!!)
      )
    }
    return from
  }
}
