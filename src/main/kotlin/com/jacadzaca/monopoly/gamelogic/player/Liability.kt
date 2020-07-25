package com.jacadzaca.monopoly.gamelogic.player

import java.math.BigInteger
import java.util.*

data class Liability(val amount: BigInteger, val recevier: Player, val recevierId: PlayerID)
