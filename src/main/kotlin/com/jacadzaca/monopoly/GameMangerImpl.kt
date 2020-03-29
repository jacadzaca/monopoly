package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket
import io.vertx.core.http.WebSocketFrame

class GameMangerImpl: GameManager {
  private val players = mutableListOf<Player>()
  override fun newPlayer(name: String, context: ServerWebSocket) {
    context.writeTextMessage("choose username")
    context.frameHandler { handler ->
      val player = Player(handler.textData(), context)
      players.add(player)
      context.writeTextMessage(handler.textData())
    }
  }


  override fun players(): List<Player> {
    return players
  }

  override fun isFull(): Boolean {
    return players.size == 4
  }

}
