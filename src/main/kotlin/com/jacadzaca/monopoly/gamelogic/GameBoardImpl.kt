package com.jacadzaca.monopoly.gamelogic

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int,
                             private val tiles: List<Tile>,
                             private val rentCalculator: RentCalculator)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean {
    return player.id == event.committerId
  }

  override fun movePlayer(player: Player): Player {
    val currentPosition = tiles.indexOf(player.position)
    return player.copy(position = tiles[wrapMove(currentPosition, dieRoller())])
  }

  override fun collectRent(from: Player): Player {
    val tile = from.position
    if (tile.owner != null && tile.owner != from.id) {
      return from.copy(
        liability = Liability(rentCalculator.getTotalRentFor(tile), tile.owner!!))
    }
    return from
  }

  private fun wrapMove(currentPosition: Int, move: Int): Int {
    return (currentPosition + move) % boardSize
  }
}
