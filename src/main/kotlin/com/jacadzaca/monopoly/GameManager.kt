package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket

interface GameManager {
  fun newPlayer(context: ServerWebSocket)
  fun players(): List<Player>
  fun isFull(): Boolean
}
