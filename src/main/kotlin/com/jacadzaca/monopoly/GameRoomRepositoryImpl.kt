package com.jacadzaca.monopoly

import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*

class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getByName(name: String): GameRoom? =
    vertx
      .eventBus()
      .requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, name)
      .body()

  override suspend fun update(name: String, updateWith: GameRoom): UpdateResult =
    vertx
      .eventBus()
      .requestAwait<UpdateResult>(
        GameRoomUpdateVerticle.ADDRESS,
        updateWith,
        deliveryOptionsOf()
          .addHeader(GameRoomUpdateVerticle.ROOMS_NAME, name)
      ).body()
}
