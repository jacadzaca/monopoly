package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gameroom.GameRoomCreationVerticle.Companion.ROOMS_NAME
import io.vertx.core.*
import io.vertx.kotlin.core.eventbus.*

internal class GameRoomRepositoryImpl internal constructor(private val vertx: Vertx) : GameRoomRepository {
  override suspend fun getById(id: String): GameRoom? =
    vertx
      .eventBus()
      .requestAwait<GameRoom>(
        GameRoomLookupVerticle.ADDRESS,
        id,
        deliveryOptionsOf(codecName = GameRoomCodec.name())
      )
      .body()

  override suspend fun saveIfAbsent(id: String, room: GameRoom): ComputationResult<Unit> {
    val result = vertx
      .eventBus()
      .requestAwait<Int>(
        GameRoomCreationVerticle.ADDRESS,
        room,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to id), codecName = GameRoomCodec.name())
      )
      .body()
    return when (result) {
      GameRoomCreationVerticle.SUCCESS -> GameRoomRepository.SUCCESS
      GameRoomCreationVerticle.NAME_TAKEN -> GameRoomRepository.NAME_TAKEN
      else -> throw IllegalStateException("Unknown error code for $result")
    }
  }

  override suspend fun update(id: String, updateWith: GameRoom): ComputationResult<Unit> {
    val result = vertx
      .eventBus()
      .requestAwait<Int>(
        GameRoomUpdateVerticle.ADDRESS,
        updateWith,
        deliveryOptionsOf(headers = mapOf(ROOMS_NAME to id), codecName = GameRoomCodec.name())
      )
      .body()
    return when (result) {
      GameRoomUpdateVerticle.SUCCESS -> GameRoomRepository.SUCCESS
      GameRoomUpdateVerticle.ALREADY_CHANGED -> GameRoomRepository.ALREADY_CHANGED
      GameRoomUpdateVerticle.NO_ROOM_WITH_NAME -> GameRoomRepository.NO_ROOM_WITH_NAME
      else -> throw IllegalStateException("Unknown error code for $result")
    }
  }
}
