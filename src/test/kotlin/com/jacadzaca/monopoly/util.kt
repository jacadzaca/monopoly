package com.jacadzaca.monopoly

import kotlin.random.*

fun Random.nextPositive(from: Int = 1, until: Int = Int.MAX_VALUE): Int {
  if (from <= 0) {
    throw IllegalArgumentException("Random.nextPositive: from must be greater than zero")
  }
  return nextInt(from, until)
}

fun Random.nextString(): String {
  return generateSequence { nextInt(Char.MIN_VALUE.toInt(), Char.MAX_VALUE.toInt()) }
    .map(Int::toChar)
    .take(200)
    .toString()
}
