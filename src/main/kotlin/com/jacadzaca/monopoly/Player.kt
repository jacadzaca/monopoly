package com.jacadzaca.monopoly

import java.util.*

data class Player(val id: UUID = UUID.randomUUID(), val piece: Piece = Piece()) {
  /**
   * @return a string that can be used to create a hash in Redis
   */
  fun redisHashDescription(): List<String> {
    return "player:$id piece:position ${piece.position}".split(' ')
  }
}
