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

    // Suppressing this is not wrong, because we only cast when the generic field is null (i.e, when the computation is a failure)
    @Suppress("UNCHECKED_CAST")
    inline fun <R> map(transform: (value: T) -> Computation<R>): Computation<R> {
        return when {
            value != null -> transform(value)
            message != null -> this as Computation<R>
            else -> throw IllegalStateException("Computation is neither a failure nor a success")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Computation<*>

        if (value != other.value) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Computation(value=$value, message=$message)"
    }
}
