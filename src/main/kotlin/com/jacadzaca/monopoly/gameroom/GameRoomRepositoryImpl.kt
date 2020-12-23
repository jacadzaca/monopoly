package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*
import java.util.*

internal class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getById(roomsName: String): Computation<GameRoom> {
    return vertx
      .eventBus()
      .requestAwait<Computation<GameRoom>>(
        GameRoomLookupVerticle.ADDRESS,
        roomsName,
      )
      .body()
  }

  override suspend fun createGameRoom(roomsName: String): Computation<Unit> {
    return vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomCreationVerticle.ADDRESS,
        roomsName,
      )
      .body()
  }

  override suspend fun request(roomsName: String, requestersId: UUID, request: Request): Computation<Unit> {
    return vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        RequestProcessingVerticle.ADDRESS,
        request,
        deliveryOptionsOf(headers = mapOf("roomsName" to roomsName, "requestersId" to requestersId.toString()))
      )
      .body()
  }
}

