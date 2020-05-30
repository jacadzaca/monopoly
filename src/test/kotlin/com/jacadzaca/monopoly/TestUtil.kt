package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import java.math.BigInteger
import java.util.*

fun getTestPlayer(): Player = Player(UUID.randomUUID(), Piece(), 123.toBigInteger(), null)
fun getTestGameEvent(): GameEvent = GameEvent(UUID.randomUUID(), 1)
fun createLiability(toWhom: UUID = UUID.randomUUID(), howMuch: BigInteger = 123.toBigInteger()): Liability = Liability(howMuch, toWhom)
fun createField(owner: UUID = UUID.randomUUID()): Field = Field(listOf(), owner)
