package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

internal class GameBoardImpl(private val boardSize: Int,
                             private val dieRoller: () -> Int,
                             private val tiles: List<Tile>,
                             private val rentCalculator: RentCalculator)
  : GameBoard {
  override fun canPlayerExecuteAction(player: Player, event: GameEvent): Boolean {
    return player.id == event.committerId
  }

  override fun movePlayer(player: Player): Player {
    val currentPosition = tiles.indexOf(player.piece.position)
    return player.copy(piece = Piece(position = tiles[wrapMove(currentPosition, dieRoller())]))
  }

  override fun collectRent(from: Player): Player {
    val tile = from.piece.position
    if (tile.owner != null && tile.owner != from.id) {
      return from.copy(
        liability = Liability(rentCalculator.getTotalRentFor(tile), tile.owner))
    }
    return from
  }

  override fun addFunds(to: Player, howMuch: BigInteger): Player {
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $to $howMuch")
    }
    return to.copy(balance = to.balance + howMuch)
  }

  override fun detractFunds(from: Player, howMuch: BigInteger): Player {
    if (from.balance < howMuch) {
      throw IllegalArgumentException("$from has insufficient funds")
    }
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $from $howMuch")
    }
    return from.copy(balance = from.balance - howMuch)
  }

  private fun wrapMove(currentPosition: Int, move: Int): Int {
    return (currentPosition + move) % boardSize
  }

}
