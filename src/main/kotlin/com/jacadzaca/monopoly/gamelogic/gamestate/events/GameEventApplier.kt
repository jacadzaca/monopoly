package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

interface GameEventApplier<T> {
  fun apply(event: T, gameState: GameState): GameState
}
