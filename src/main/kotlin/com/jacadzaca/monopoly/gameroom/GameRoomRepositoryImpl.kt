package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*

internal class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getById(id: String): ComputationResult<GameRoom> {
   return vertx
      .eventBus()
      .requestAwait<ComputationResult<GameRoom>>(
        GameRoomLookupVerticle.ADDRESS,
        id,
      )
      .body()
  }

  override suspend fun saveIfAbsent(id: String, room: GameRoom): ComputationResult<Unit> {
    return vertx
      .eventBus()
      .requestAwait<ComputationResult<Unit>>(
        GameRoomCreationVerticle.ADDRESS,
        room,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to id))
      )
      .body()
  }

  override suspend fun update(id: String, updateWith: GameRoom): ComputationResult<Unit> {
    return vertx
      .eventBus()
      .requestAwait<ComputationResult<Unit>>(
        GameRoomUpdateVerticle.ADDRESS,
        updateWith,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to id))
      )
      .body()
  }
}

