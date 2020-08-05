package com.jacadzaca.monopoly.gamelogic

sealed class ValidationResult {
  data class Success(val transformation: Transformation): ValidationResult()
  data class Failure(val reason: String): ValidationResult()
}
