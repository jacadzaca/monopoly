package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.player.Player
import java.math.BigInteger

internal class FundsManagerImpl : FundsManager {
  override fun addFunds(to: Player, howMuch: BigInteger): Player {
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $to $howMuch")
    }
    return to.copy(balance = to.balance + howMuch)
  }

  override fun detractFunds(from: Player, howMuch: BigInteger): Player {
    if (from.balance < howMuch) {
      throw IllegalArgumentException("$from has insufficient funds")
    }
    if (howMuch < BigInteger.ZERO) {
      throw IllegalArgumentException("howMuch must be positive, $from $howMuch")
    }
    return from.copy(balance = from.balance - howMuch)
  }}
