package com.jacadzaca.monopoly

import io.vertx.core.shareddata.*
import io.vertx.kotlin.core.shareddata.*
import java.util.*

class GameRoomServiceImpl(
  private val rooms: AsyncMap<UUID, GameRoom>,
  private val getLock: suspend (UUID) -> (Lock)
) : GameRoomService {
  companion object {
    internal const val invalidRoomId = "No game room with such id"
    internal const val otherChangeWasApplied = "Rooms' versions differ, cannot apply change"
  }

  override suspend fun byId(id: UUID): GameRoom? = rooms.getAwait(id)

  override suspend fun updateGameState(roomId: UUID, updateWith: GameRoom): UpdateResult {
    val lock = getLock(roomId)

    val room = rooms.getAwait(roomId)
    val result = when {
      room == null -> UpdateResult.Failure(invalidRoomId)
      room.version != updateWith.version -> UpdateResult.Failure(otherChangeWasApplied)
      else -> {
        rooms.putAwait(roomId, updateWith.copy(version = updateWith.version + 1))
        UpdateResult.Success
      }
    }

    lock.release()
    return result
  }
}
