package com.jacadzaca.monopoly

import kotlin.random.Random

fun Random.nextPositive(from: Int = 1, until: Int = Int.MAX_VALUE): Int {
    if (from <= 0) {
        throw IllegalArgumentException("Random.nextPositive: from must be greater than zero")
    }
    return nextInt(from, until)
}

fun Random.nextString(): String {
    return generateSequence { nextInt(Char.MIN_VALUE.code, Char.MAX_VALUE.code) }
        .map(Int::toChar)
        .take(200)
        .toString()
}
