package com.jacadzaca.monopoly.gamelogic.gamestate.events

import java.util.*

data class MoveEvent(override val playerId: UUID) : GameEvent
