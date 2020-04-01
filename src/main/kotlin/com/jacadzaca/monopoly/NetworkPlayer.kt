package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket

data class NetworkPlayer(
  val connection: ServerWebSocket?,
  override var piece: Piece
) : Player
