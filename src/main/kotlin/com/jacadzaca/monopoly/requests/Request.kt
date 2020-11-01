package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.commands.*

interface Request {
  companion object {
    internal val INVALID_PLAYER_ID = ComputationResult.failure<Command>("No player with specified ID exist")
    internal val BUYER_HAS_INSUFFICIENT_BALANCE =
      ComputationResult.failure<Command>("Buyer dose not have enough funds to preform requested action")
  }

  fun validate(): ComputationResult<Command>
}
