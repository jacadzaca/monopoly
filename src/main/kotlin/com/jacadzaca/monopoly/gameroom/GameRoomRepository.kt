package com.jacadzaca.monopoly.gameroom

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

  suspend fun getById(id: String): GameRoom?
  suspend fun update(id: String, updateWith: GameRoom): UpdateResult
}
