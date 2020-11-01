package com.jacadzaca.monopoly

class ComputationResult<out T> private constructor(
   val value: T?,
   val message: String?,
) {

  companion object {
    @JvmStatic
    fun <T> failure(message: String): ComputationResult<T> {
      return ComputationResult(null, message)
    }

    @JvmStatic
    fun <T> success(value: T): ComputationResult<T> {
      return ComputationResult(value, null)
    }
  }

  inline fun fold(onSuccess: (value: T) -> Unit, onFailure: (message: String) -> Unit) {
    if (value != null) {
      onSuccess(value)
    } else {
      onFailure(message!!)
    }
  }
}
