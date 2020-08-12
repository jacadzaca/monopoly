package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.transformations.Transformation

sealed class ValidationResult {
  data class Success(val transformation: Transformation) : ValidationResult()
  data class Failure(val reason: String) : ValidationResult()
}
