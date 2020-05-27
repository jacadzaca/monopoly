package com.jacadzaca.monopoly.gamelogic

import java.util.*

data class Player(val id: UUID, val piece: Piece) {
  companion object {
    fun createFromList(list: List<String>): Player =
      Player(
        UUID.fromString(list.last()),
        Piece(list[0].toInt())
      )
  }

  fun redisHashDescription(): List<String> {
    return "player:$id player:piece:position ${piece.position}".split(' ')
  }
}
