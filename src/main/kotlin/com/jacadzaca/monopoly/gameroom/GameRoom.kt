package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.GameAction
import com.jacadzaca.monopoly.gamelogic.player.Player
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface GameRoom {
  fun getCurrentPlayer(): Single<Player>
  fun publishAction(action: GameAction): Completable
  fun listenToRoom(): Flowable<GameAction>
}
