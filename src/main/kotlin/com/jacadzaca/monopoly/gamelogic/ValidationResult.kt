package com.jacadzaca.monopoly.gamelogic

sealed class ValidationResult {
  data class Success(val action: Action): ValidationResult()
  data class Failure(val reason: String): ValidationResult()
}
