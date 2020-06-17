package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import java.math.BigInteger
import java.util.UUID

data class Player(val id: UUID,
                  val position: Int = 0,
                  val balance: BigInteger,
                  val liability: Liability?) {

  fun increasePosition(by: Int): Player {
    return copy(position = position + by)
  }
}
