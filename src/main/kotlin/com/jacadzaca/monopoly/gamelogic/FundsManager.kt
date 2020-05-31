package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger

interface FundsManager {
  fun addFunds(to: Player, howMuch: BigInteger): Player
  fun detractFunds(from: Player, howMuch: BigInteger): Player
}
