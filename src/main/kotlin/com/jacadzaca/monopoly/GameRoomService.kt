package com.jacadzaca.monopoly

import java.util.*

interface GameRoomService {
  suspend fun byId(id: UUID): GameRoom?
  suspend fun updateGameState(roomId: UUID, updateWith: GameRoom): UpdateResult
}
