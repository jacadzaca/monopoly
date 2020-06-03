package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.GameBoard
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import java.math.BigInteger
import java.util.UUID

data class Player(val id: UUID,
                  val position: Tile = GameBoard.startTile,
                  val balance: BigInteger,
                  val liability: Liability?) {

  fun move(to: Tile): Player {
    return copy(position = to)
  }
}
