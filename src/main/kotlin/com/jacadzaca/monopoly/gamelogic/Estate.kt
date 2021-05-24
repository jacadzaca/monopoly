@file:UseSerializers(BigIntegerSerializer::class)

package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.BigIntegerSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigInteger

@Serializable
sealed class Estate {
    abstract val rent: BigInteger
    abstract val price: BigInteger
    @Serializable
    @SerialName("house")
    data class House(override val rent: BigInteger, override val price: BigInteger) : Estate()
    @Serializable
    @SerialName("hotel")
    data class Hotel(override val rent: BigInteger, override val price: BigInteger) : Estate()
}
