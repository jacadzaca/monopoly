package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.player.Player
import java.math.BigInteger

interface FundsManager {
  fun addFunds(to: Player, howMuch: BigInteger): Player
  fun detractFunds(from: Player, howMuch: BigInteger): Player
}
