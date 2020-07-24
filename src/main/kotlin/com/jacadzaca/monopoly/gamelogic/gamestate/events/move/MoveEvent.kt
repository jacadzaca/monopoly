package com.jacadzaca.monopoly.gamelogic.gamestate.events.move

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.GameEvent
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class MoveEvent(override val playerId: PlayerID) : GameEvent
