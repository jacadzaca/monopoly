package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import io.vertx.core.buffer.*
import io.vertx.core.shareddata.impl.*

data class GameRoom(val gameState: GameState, val version: Long = 0L): ClusterSerializable {
  override fun readFromBuffer(pos: Int, buffer: Buffer): Int {
    throw UnsupportedOperationException()
  }

  override fun writeToBuffer(buffer: Buffer) {
    throw UnsupportedOperationException()
  }
}
