package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.Player
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface GameRoom {
  fun getCurrentPlayer(): Single<Player>
  fun publishAction(action: GameAction): Completable
  fun listenToRoom(): Flowable<GameAction>
}
