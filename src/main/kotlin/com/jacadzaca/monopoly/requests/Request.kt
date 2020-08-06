package com.jacadzaca.monopoly.requests

interface Request {
  companion object {
    internal const val invalidPlayerId = "No player with specified ID exist"
    internal const val buyerHasInsufficientBalance = "Buyer dose not have enough funds to preform requested action"
  }
  fun validate(): ValidationResult
}
