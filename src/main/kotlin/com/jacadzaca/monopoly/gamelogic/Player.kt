@file:UseSerializers(BigIntegerSerializer::class)

package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import java.math.BigInteger

@Serializable
data class Player(
  val position: Int = 0,
  val balance: BigInteger
) {
  fun setPosition(newPosition: Int): Player = copy(position = newPosition)
  fun setBalance(newBalance: BigInteger): Player = copy(balance =  newBalance)
  fun addFunds(toAdd: BigInteger): Player = copy(balance = balance + toAdd)
  fun detractFunds(toDetract: BigInteger): Player = copy(balance = balance - toDetract)
}
