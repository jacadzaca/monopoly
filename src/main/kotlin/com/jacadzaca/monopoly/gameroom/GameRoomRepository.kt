package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import io.vertx.core.eventbus.*

interface GameRoomRepository {
  fun getGameState(roomsName: String): Future<GameState>
  fun subscribe(roomsName: String): MessageConsumer<Event>
  fun sendRequest(request: Request, roomsName: String): Future<Unit>
}
