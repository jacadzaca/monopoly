package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger
import java.util.UUID

data class Player(val id: UUID,
                  val piece: Piece,
                  val balance: BigInteger,
                  val liability: Liability?)
