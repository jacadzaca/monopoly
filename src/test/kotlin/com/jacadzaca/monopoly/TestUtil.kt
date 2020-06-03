package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import java.math.BigInteger
import java.util.*

fun getTestPlayer(): Player =
    Player(
        UUID.randomUUID(),
        createTile(null),
        123.toBigInteger(),
        null
    )
fun getTestGameEvent(): GameEvent = GameEvent(UUID.randomUUID(), 1)
fun createLiability(toWhom: UUID = UUID.randomUUID(), howMuch: BigInteger = 123.toBigInteger()): Liability =
  Liability(howMuch, toWhom)
fun createTile(owner: UUID? = UUID.randomUUID()): Tile = Tile(listOf(), 0.toBigInteger(), owner)
