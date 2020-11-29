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

  inline fun onSuccess(onSuccess: (value: T) -> Unit): ComputationResult<T> {
    if (value == null) return this
    onSuccess(value)
    return this
  }

  inline fun onFailure(onFailure: (message: String) -> Unit): ComputationResult<T> {
    if (message == null) return this
    onFailure(message)
    return this
  }

  inline fun <R> flatMap(flatMap: (value: T) -> ComputationResult<R>): ComputationResult<R> {
    return when {
      value != null -> flatMap(value)
      message != null -> failure(message)
      else -> throw IllegalStateException("Computation is neither a failure nor a success")
    }
  }
}
