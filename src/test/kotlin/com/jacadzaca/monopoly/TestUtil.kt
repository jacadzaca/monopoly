package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import kotlinx.collections.immutable.persistentListOf
import java.math.BigInteger
import java.util.*

fun createPlayer(startPosition: Int = 0): Player =
  Player(
    startPosition,
    123.toBigInteger()
  )
fun createHouse(rent: BigInteger = 100.toBigInteger()) = Estate(rent, EstateType.HOUSE)
fun createHotel(rent: BigInteger = 120.toBigInteger()) = Estate(rent, EstateType.HOTEL)
fun createTile(owner: UUID? = UUID.randomUUID()): Tile =
    Tile(persistentListOf(), 0.toBigInteger(), owner)
