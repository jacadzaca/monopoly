package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

interface RoomManager {
  fun isPlayersTurn(playerId: UUID, roomId: UUID): Single<Boolean>
  fun publishAction(roomId: UUID, action: GameAction)
  fun listenToRoom(roomId: UUID): Flowable<GameAction>
}
