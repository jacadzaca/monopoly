package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

interface GameEventVerifier<T : GameEvent> {
  fun verify(event: T, gameState: GameState): T?
}
