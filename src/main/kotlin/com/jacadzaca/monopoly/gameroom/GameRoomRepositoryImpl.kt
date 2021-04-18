package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import io.vertx.core.eventbus.*
import io.vertx.kotlin.core.eventbus.*

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
