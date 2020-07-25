package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.EstatePurchaseEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.move.MoveEventVerifier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEventVerifier

interface GameEventVerifier<in T> {
  companion object {
    val standardMoveEventVerifier: GameEventVerifier<GameEvent.MoveEvent> = MoveEventVerifier()
    val standardTilePurchaseEventVerifier: GameEventVerifier<GameEvent.TilePurchaseEvent> =
      TilePurchaseEventVerifier(::tileExists)
    val standardEstatePurchaseEventVerifier: GameEventVerifier<GameEvent.EstatePurchaseEvent> = EstatePurchaseEventVerifier(
      EstateFactory.standardEstateFactory, 4, ::tileExists
    )
    internal const val invalidPlayerId = "No player with specified ID exist"
    internal const val invalidTileIndex = "There exist no tile at specified tile index"
    internal const val buyerHasInsufficientBalance = "Buyer dose not have enough funds to preform requested action"
  }

  fun verify(event: T, gameState: GameState): VerificationResult
}
