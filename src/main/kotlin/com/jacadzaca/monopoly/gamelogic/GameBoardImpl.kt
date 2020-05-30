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
    val position = wrapMove(player.piece.position, dieRoller())
    val tile = tiles[position]
    if (tile.owner != null && tile.owner != player.id) {
      return player.copy(
        piece = Piece(position),
        liability = Liability(rentCalculator.getTotalRentFor(tile), tile.owner))
    }
    return player.copy(piece = Piece(position))
  }

  override fun addFunds(player: Player, howMuch: BigInteger): Player {
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $player $howMuch")
    }
    return player.copy(balance = player.balance + howMuch)
  }

  override fun detractFunds(player: Player, howMuch: BigInteger): Player {
    if (player.balance < howMuch) {
      throw IllegalArgumentException("$player has insufficient funds")
    }
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $player $howMuch")
    }
    return player.copy(balance = player.balance - howMuch)
  }

  private fun wrapMove(currentPosition: Int, move: Int): Int {
    return (currentPosition + move) % boardSize
  }

}
