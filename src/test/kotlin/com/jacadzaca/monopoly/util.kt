package com.jacadzaca.monopoly

import kotlin.random.*

fun Random.nextPositive(from: Int = 1, until: Int = Int.MAX_VALUE): Int = nextInt(from, until)
