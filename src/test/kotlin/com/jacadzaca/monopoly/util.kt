package com.jacadzaca.monopoly

import kotlin.random.*

fun Random.nextPositive(from: Int = 1): Int = nextInt(from, Int.MAX_VALUE)
