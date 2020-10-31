package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.commands.*

interface Request {
  companion object {
    internal val INVALID_PLAYER_ID = Result.failure<Command>(ValidationException("No player with specified ID exist"))
    internal val BUYER_HAS_INSUFFICIENT_BALANCE =
      Result.failure<Command>(ValidationException("Buyer dose not have enough funds to preform requested action"))
  }

  fun validate(): Result<Command>

  class ValidationException(message: String) : Throwable(message)
}
