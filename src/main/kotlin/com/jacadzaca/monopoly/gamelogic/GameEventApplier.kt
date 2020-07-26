package com.jacadzaca.monopoly.gamelogic

interface GameEventApplier<T> {
  fun apply(event: T, gameState: GameState): GameState
}
