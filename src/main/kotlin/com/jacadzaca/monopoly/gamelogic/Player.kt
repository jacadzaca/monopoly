package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

data class Player(
  val position: Int = 0,
  val balance: BigInteger
) {
  fun addFunds(toAdd: BigInteger): Player = copy(balance = balance + toAdd)
  fun detractFunds(toDetract: BigInteger): Player = copy(balance = balance - toDetract)
}
