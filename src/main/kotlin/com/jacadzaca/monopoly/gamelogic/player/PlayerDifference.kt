package com.jacadzaca.monopoly.gamelogic.player

import com.jacadzaca.monopoly.gamelogic.Difference
import java.math.BigInteger

data class PlayerDifference(val changeInPosition: Int? = null,
                            val changeInBalance: BigInteger? = null,
                            val changeInLiability: Liability? = null) : Difference
