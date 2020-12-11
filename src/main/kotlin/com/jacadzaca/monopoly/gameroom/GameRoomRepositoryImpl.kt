package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*
import kotlinx.serialization.builtins.*

internal class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getById(id: String): Computation<GameRoom> {
    return vertx
      .eventBus()
      .requestAwait<Computation<GameRoom>>(
        GameRoomLookupVerticle.ADDRESS,
        id,
      )
      .body()
  }

  override suspend fun createGameRoom(id: String): Computation<Unit> {
    return vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomCreationVerticle.ADDRESS,
        id,
      )
      .body()
  }

  override suspend fun update(id: String, updateWith: GameRoom): Computation<Unit> {
    return vertx
      .eventBus()
      .requestAwait<Computation<Unit>>(
        GameRoomUpdateVerticle.ADDRESS,
        updateWith,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to id))
      )
      .body()
  }
}

