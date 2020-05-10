package com.jacadzaca.monopoly

import io.reactivex.Completable
import io.reactivex.Single

interface Player {
  fun updatePosition(newPosition: Int): Completable
  fun getPosition(): Single<Int>
}
