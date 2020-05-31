package com.jacadzaca.monopoly.gamelogic

import java.math.BigInteger
import java.util.UUID

data class Player(val id: UUID,
                  val position: Tile = GameBoard.startTile,
                  val balance: BigInteger,
                  val liability: Liability?)
