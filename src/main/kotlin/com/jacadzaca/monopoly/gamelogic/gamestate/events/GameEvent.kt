package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.PlayerID

interface GameEvent {
  val playerId: PlayerID
}
