package com.jacadzaca.monopoly.gamelogic.player

import java.math.BigInteger

data class Player(val id: PlayerID,
                  val position: Int = 0,
                  val balance: BigInteger,
                  val liability: Liability?) {
  fun addFunds(toAdd: BigInteger): Player = copy(balance = balance + toAdd)
  fun detractFunds(toDetract: BigInteger): Player = copy(balance = balance - toDetract)

}
