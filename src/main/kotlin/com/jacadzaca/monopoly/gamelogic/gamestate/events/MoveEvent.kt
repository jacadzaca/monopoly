package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class MoveEvent(override val playerId: PlayerID) : GameEvent
