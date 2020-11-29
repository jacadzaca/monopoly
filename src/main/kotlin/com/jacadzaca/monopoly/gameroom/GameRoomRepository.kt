package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.*
import java.lang.IllegalStateException

interface GameRoomRepository {
  companion object {
    private var instance: GameRoomRepositoryImpl? = null

    fun instance(vertx: Vertx): GameRoomRepository {

      if (instance == null) {
        instance = GameRoomRepositoryImpl(vertx)
      }
      return instance ?: throw IllegalStateException("Set to null by another thread")
    }
  }

  suspend fun getById(id: String): ComputationResult<GameRoom>
  suspend fun saveIfAbsent(id: String, room: GameRoom): ComputationResult<Unit>
  suspend fun update(id: String, updateWith: GameRoom): ComputationResult<Unit>
}
