package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket
import io.vertx.core.http.WebSocketFrame

data class Player(val name: String, val context: ServerWebSocket, val isHost: Boolean = false)
