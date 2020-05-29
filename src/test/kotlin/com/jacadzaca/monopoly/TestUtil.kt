package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.GameEvent
import com.jacadzaca.monopoly.gamelogic.Piece
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.*

fun getTestPlayer(): Player = Player(UUID.randomUUID(), Piece(), 123.toBigInteger(), listOf())
fun getTestGameEvent(): GameEvent = GameEvent(UUID.randomUUID(), 1)
