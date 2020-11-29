package com.jacadzaca.monopoly

import kotlinx.serialization.Serializable

@Serializable
class Computation<out T> private constructor(
  val value: T?,
  val message: String?,
) {

  companion object {
    @JvmStatic
    fun <T> failure(message: String): Computation<T> {
      return Computation(null, message)
    }

    @JvmStatic
    fun <T> success(value: T): Computation<T> {
      return Computation(value, null)
    }
  }

  inline fun onSuccess(onSuccess: (value: T) -> Unit): Computation<T> {
    if (value == null) return this
    onSuccess(value)
    return this
  }

  inline fun onFailure(onFailure: (message: String) -> Unit): Computation<T> {
    if (message == null) return this
    onFailure(message)
    return this
  }

  inline fun <R> flatMap(flatMap: (value: T) -> Computation<R>): Computation<R> {
    return when {
      value != null -> flatMap(value)
      message != null -> failure(message)
      else -> throw IllegalStateException("Computation is neither a failure nor a success")
    }
  }
}
