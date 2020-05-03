package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

interface GameRoom {
  fun isPlayersTurn(playerId: UUID): Single<Boolean>
  fun publishAction(action: GameAction): Completable
  fun listenToRoom(): Flowable<GameAction>
}
