package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket
import io.vertx.core.http.WebSocketFrame
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.random.Random.Default.nextInt

class GameMangerImpl: GameManager {
  private val players: Deque<Player> = LinkedList()

  override fun newPlayer(context: ServerWebSocket) {
    if (isFull()) {
      throw IllegalStateException("Room $this full")
    }
    val player = if (players.size == 0) Player(nextInt().toString(), context, isHost = true)
                 else Player(nextInt().toString(), context)
    players.add(player)
  }


  override fun players(): List<Player> {
    return ArrayList(players)
  }

  override fun isFull(): Boolean {
    return players.size == 4
  }

}
