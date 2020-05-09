package com.jacadzaca.monopoly

import io.reactivex.Completable

interface Player {
  fun updatePosition(newPosition: Int): Completable
}
