package com.jacadzaca.monopoly.requests
import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

interface Request {
  companion object {
    internal val INVALID_PLAYER_ID = Computation.failure<Command>("No player with specified ID exist")
    internal val BUYER_HAS_INSUFFICIENT_BALANCE =
      Computation.failure<Command>("Buyer dose not have enough funds to preform requested action")
  }

  fun validate(context: GameState): Computation<Command>
  fun playersId(): UUID
}
