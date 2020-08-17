package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.commands.Command

sealed class ValidationResult {
  data class Success(val command: Command) : ValidationResult()
  data class Failure(val reason: String) : ValidationResult()
}
