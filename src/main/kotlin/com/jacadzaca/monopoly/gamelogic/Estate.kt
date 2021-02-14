@file:UseSerializers(BigIntegerSerializer::class)

package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import java.math.*

@Serializable
sealed class Estate {
  abstract val rent: BigInteger
  abstract val price: BigInteger
  @Serializable
  data class House(override val rent: BigInteger, override val price: BigInteger) : Estate()
  @Serializable
  data class Hotel(override val rent: BigInteger, override val price: BigInteger) : Estate()
}
