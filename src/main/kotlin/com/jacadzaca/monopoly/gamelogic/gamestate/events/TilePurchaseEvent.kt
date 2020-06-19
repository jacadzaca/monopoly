package com.jacadzaca.monopoly.gamelogic.gamestate.events

import java.util.*

data class TilePurchaseEvent(override val playerId: UUID,
                             val tileIndex: Int) : GameEvent
