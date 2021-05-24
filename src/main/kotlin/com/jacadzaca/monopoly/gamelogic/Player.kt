@file:UseSerializers(BigIntegerSerializer::class)

package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.BigIntegerSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigInteger

@Serializable
data class Player(
    val position: Int = 0,
    val balance: BigInteger,
    val name: String? = null,
) {
    fun setPosition(newPosition: Int): Player = copy(position = newPosition)
    fun setBalance(newBalance: BigInteger): Player = copy(balance = newBalance)
    fun setName(newName: String): Player = copy(name = newName)
}
