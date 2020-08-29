package com.jacadzaca.monopoly

import kotlinx.serialization.Serializable

@Serializable
sealed class UpdateResult {
  @Serializable
  object Success : UpdateResult()
  @Serializable
  data class Failure(val reason: String) : UpdateResult()
}
