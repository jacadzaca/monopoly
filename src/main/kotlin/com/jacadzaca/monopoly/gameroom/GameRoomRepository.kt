package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.Delta
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.requests.Request
import io.vertx.core.Future
import io.vertx.core.eventbus.MessageConsumer

interface GameRoomRepository {
    fun getGameState(roomsName: String): Future<GameState>
    fun subscribe(roomsName: String): MessageConsumer<Delta>
    fun sendRequest(request: Request, roomsName: String): Future<Unit>
}
