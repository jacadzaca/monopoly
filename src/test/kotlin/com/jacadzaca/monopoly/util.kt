package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import kotlinx.collections.immutable.*
import kotlin.random.*

fun Random.nextPositive(from: Int = 1, until: Int = Int.MAX_VALUE): Int {
  if (from <= 0) {
    throw IllegalArgumentException("Random.nextPositive: from must be greater than zero")
  }
  return nextInt(from, until)
}

fun createHouses(): PersistentList<Estate> {
  return (1..10)
    .map { Estate.House(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
    .toPersistentList()
}

fun createHotels(): PersistentList<Estate> {
  return (1..10)
    .map { Estate.Hotel(Random.nextPositive().toBigInteger(), Random.nextPositive().toBigInteger()) }
    .toPersistentList()
}
