package com.jacadzaca.monopoly.gamelogic

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int,
                             private val tiles: List<Tile>,
                             private val rentCalculator: RentCalculator)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean {
    return player.id == event.committerId
  }

  override fun collectRent(from: Player): Player {
    val tile = from.position
    if (tile.owner != null && tile.owner != from.id) {
      return from.copy(
        liability = Liability(rentCalculator.getTotalRentFor(tile), tile.owner!!))
    }
    return from
  }
}
