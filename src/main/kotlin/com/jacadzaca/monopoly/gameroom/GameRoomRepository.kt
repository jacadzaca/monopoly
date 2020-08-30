package com.jacadzaca.monopoly.gameroom

import io.vertx.core.*
import java.lang.IllegalStateException

interface GameRoomRepository {
  companion object {
    private var instance: GameRoomRepositoryImpl? = null

    fun instance(vertx: Vertx): GameRoomRepositoryImpl {
      if (instance == null) {
        instance = GameRoomRepositoryImpl(vertx)
      }
      return instance ?: throw IllegalStateException("Set to null by another thread")
    }
  }

  suspend fun getByName(name: String): GameRoom?
  suspend fun update(name: String, updateWith: GameRoom): UpdateResult
}
