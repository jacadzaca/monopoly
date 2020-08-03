package com.jacadzaca.monopoly.gamelogic

import java.util.*

sealed class Transformation {
  data class PlayerMovement(val movedPlayerId: UUID, val movedBy: Int) : Transformation()
}
