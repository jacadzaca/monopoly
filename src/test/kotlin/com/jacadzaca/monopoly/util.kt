package com.jacadzaca.monopoly

import java.math.BigInteger
import kotlin.random.Random

fun randomPositive(): Int {
  return Random.nextInt(1, Int.MAX_VALUE)
}

fun randomPositiveBIG(): BigInteger {
  return Random.nextInt(1, Int.MAX_VALUE).toBigInteger()
}
