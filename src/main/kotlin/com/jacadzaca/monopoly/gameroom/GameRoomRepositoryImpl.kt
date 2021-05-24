package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.Delta
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.requests.Request
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf

class GameRoomRepositoryImpl(private val vertx: Vertx) : GameRoomRepository {
    private companion object {
        private val requestCodec = deliveryOptionsOf(codecName = "requestCodec")
    }

    override fun getGameState(roomsName: String): Future<GameState> {
        return vertx.eventBus().request<GameState>("${roomsName}LOOKUP", null).map { it.body() }
    }

    override fun subscribe(roomsName: String): MessageConsumer<Delta> {
        return vertx.eventBus().consumer("${roomsName}INFO")
    }

    override fun sendRequest(request: Request, roomsName: String): Future<Unit> {
        return vertx
            .eventBus()
            .request<Unit>(roomsName, request, requestCodec)
            .map { }
    }
}
