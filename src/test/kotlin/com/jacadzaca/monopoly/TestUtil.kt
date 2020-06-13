package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import kotlinx.collections.immutable.persistentListOf
import java.math.BigInteger
import java.util.*

fun getTestPlayer(): Player =
    Player(
        UUID.randomUUID(),
        0,
        123.toBigInteger(),
        null
    )
fun getTestGameEvent(): GameAction = GameAction(UUID.randomUUID(), 1)
fun createHouse(rent: BigInteger = 100.toBigInteger()) = Building(rent, BuildingType.HOUSE)
fun createHotel(rent: BigInteger = 120.toBigInteger()) = Building(rent, BuildingType.HOTEL)
fun createTile(owner: UUID? = UUID.randomUUID()): Tile = Tile(persistentListOf(), 0.toBigInteger(), owner)
