package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.Delta
import java.math.BigInteger

data class PlayerDelta(val changeInPosition: Int? = null,
                       val changeInBalance: BigInteger? = null,
                       val changeInLiability: Liability? = null) : Delta
