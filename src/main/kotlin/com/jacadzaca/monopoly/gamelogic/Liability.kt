package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger
import java.util.*

data class Liability(val amount: BigInteger, val recevier: Player, val recevierId: UUID)
