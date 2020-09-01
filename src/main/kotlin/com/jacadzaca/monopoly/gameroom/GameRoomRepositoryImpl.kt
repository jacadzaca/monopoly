package com.jacadzaca.monopoly.gameroom

import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*

internal class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getById(id: String): GameRoom? =
    vertx
      .eventBus()
      .requestAwait<GameRoom>(GameRoomLookupVerticle.ADDRESS, id)
      .body()

  override suspend fun save(id: String, room: GameRoom) =
    vertx
      .eventBus()
      .requestAwait<UpdateResult>(
        GameRoomUpdateVerticle.ADDRESS,
        room,
        deliveryOptionsOf()
          .addHeader(GameRoomUpdateVerticle.ROOMS_NAME, id)
      ).body()!!
}
