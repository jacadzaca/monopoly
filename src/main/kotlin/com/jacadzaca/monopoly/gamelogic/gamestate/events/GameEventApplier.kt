package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.EstatePurchaseApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.move.MoveEventApplier
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEventApplier
import com.jacadzaca.monopoly.gamelogic.player.DiceRollPlayerMover
import kotlin.random.Random

interface GameEventApplier<T> {
  companion object {
    val tilePurchaseEventApplier: GameEventApplier<VerificationResult.VerifiedTilePurchaseEvent> =
      TilePurchaseEventApplier()
    val moveEventApplier: GameEventApplier<VerificationResult.VerifiedMoveEvent> =
      MoveEventApplier(DiceRollPlayerMover { Random.nextInt(1, 6) })
    val estatePurchaseEventApplier: GameEventApplier<VerificationResult.VerifiedEstatePurchaseEvent> =
      EstatePurchaseApplier(
        EstateFactory.standardEstateFactory
      )
  }

  fun apply(event: T, gameState: GameState): GameState
}
