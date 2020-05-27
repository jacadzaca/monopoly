package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger
import java.util.UUID

data class Player(val id: UUID, val piece: Piece, val balance: BigInteger) {
  companion object {
    fun createFromList(list: List<String>): Player =
      Player(
        UUID.fromString(list.last()),
        Piece(list[0].toInt()),
        list[1].toBigInteger())
  }

  fun redisHashDescription(): List<String> {
    return "player:$id player:piece:position ${piece.position} player:balance $balance".split(' ')
  }
}
