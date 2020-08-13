package com.jacadzaca.monopoly

sealed class UpdateResult {
  object Success : UpdateResult()
  data class Failure(val reason: String): UpdateResult()
}
