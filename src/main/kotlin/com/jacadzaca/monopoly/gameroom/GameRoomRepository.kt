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

    internal val SUCCESS = ComputationResult.success(Unit)
    internal val NAME_TAKEN = ComputationResult.failure<Unit>("Cannot create a game room with given name")
    internal val ALREADY_CHANGED = ComputationResult.failure<Unit>("Changes were applied to given game room while processing the request")
    internal val NO_ROOM_WITH_NAME = ComputationResult.failure<Unit>("Cannot update desired room, it dose not seem to exist")
  }

  suspend fun getById(id: String): GameRoom?
  suspend fun saveIfAbsent(id: String, room: GameRoom): ComputationResult<Unit>
  suspend fun update(id: String, updateWith: GameRoom): ComputationResult<Unit>
}
