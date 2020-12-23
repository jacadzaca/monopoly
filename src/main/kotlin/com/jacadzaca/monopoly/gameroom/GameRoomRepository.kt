package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.requests.*
import io.vertx.core.*
import java.lang.IllegalStateException
import java.util.*

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

  suspend fun getById(roomsName: String): Computation<GameRoom>
  suspend fun createGameRoom(roomsName: String): Computation<Unit>
  suspend fun request(roomsName: String, requestersId: UUID, request: Request): Computation<Unit>
}
