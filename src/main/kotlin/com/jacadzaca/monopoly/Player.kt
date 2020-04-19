package com.jacadzaca.monopoly

import java.util.UUID

data class Player(val id: UUID = UUID.randomUUID(), val piece: Piece = Piece()) {
  companion object {
    @JvmStatic
    fun createFromList(list: List<String>): Player = Player(UUID.fromString(list.last()), Piece(list[0].toInt()))
  }
  /**
   * @return a string that can be used to create a hash in Redis
   */
  fun redisHashDescription(): List<String> {
    return "player:$id piece:position ${piece.position}".split(' ')
  }
}
