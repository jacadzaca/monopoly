package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

interface GameEventVerifier<T : GameEvent> {
  companion object {
    internal const val invalidPlayerId = "No player with specified ID exist"
    internal const val invalidTileIndex = "There exist no tile at specified tile index"
    internal const val buyerHasInsufficientBalance = "Buyer dose not have enough funds to preform requested action"
  }

  fun verify(event: T, gameState: GameState): VerificationResult
}
